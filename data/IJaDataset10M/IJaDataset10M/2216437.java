package zildo.fwk.gfx.engine;

import java.util.ArrayList;
import java.util.List;
import zildo.Zildo;
import zildo.fwk.bank.MotifBank;
import zildo.fwk.gfx.GFXBasics;
import zildo.fwk.gfx.effect.CloudGenerator;
import zildo.fwk.gfx.primitive.TileGroupPrimitive;
import zildo.monde.map.Area;
import zildo.monde.map.Case;
import zildo.monde.map.Tile;
import zildo.monde.util.Point;
import zildo.resource.Constantes;

public abstract class TileEngine {

    private int cameraX;

    private int cameraY;

    protected TileGroupPrimitive meshFORE;

    protected TileGroupPrimitive meshBACK;

    protected boolean initialized = false;

    List<MotifBank> motifBanks;

    public int texCloudId;

    protected TextureEngine textureEngine;

    public static String[] tileBankNames = { "foret1", "village", "maison", "grotte", "foret2", "foret3", "foret4", "palais1", "palais2" };

    public TileEngine(TextureEngine texEngine) {
        super();
        textureEngine = texEngine;
        cameraX = -1;
        cameraY = -1;
        meshFORE = new TileGroupPrimitive(Constantes.NB_MOTIFBANK);
        meshBACK = new TileGroupPrimitive(Constantes.NB_MOTIFBANK);
        motifBanks = new ArrayList<MotifBank>();
        this.charge_tous_les_motifs();
        loadTiles();
        createCloudTexture();
    }

    public void cleanUp() {
        meshFORE.cleanUp();
        meshBACK.cleanUp();
        initialized = false;
    }

    void charge_tous_les_motifs() {
        for (String bankName : tileBankNames) {
            this.charge_motifs(bankName.toLowerCase());
        }
    }

    void charge_motifs(String filename) {
        MotifBank motifBank = new MotifBank();
        motifBank.charge_motifs(filename);
        motifBanks.add(motifBank);
    }

    public MotifBank getMotifBank(int n) {
        return motifBanks.get(n);
    }

    public void loadTiles() {
        for (int i = 0; i < tileBankNames.length; i++) {
            MotifBank motifBank = getMotifBank(i);
            this.createTextureFromMotifBank(motifBank);
        }
    }

    public void createTextureFromMotifBank(MotifBank mBank) {
        GFXBasics surface = textureEngine.prepareSurfaceForTexture(true);
        int x = 0, y = 0;
        for (int n = 0; n < mBank.getNb_motifs(); n++) {
            short[] motif = mBank.get_motif(n);
            for (int j = 0; j < 16; j++) {
                for (int i = 0; i < 16; i++) {
                    int a = motif[i + j * 16];
                    if (a != 255) {
                        surface.pset(i + x, j + y, a, null);
                    }
                }
            }
            x += 16;
            if (x >= 256) {
                x = 0;
                y += 16;
            }
        }
        textureEngine.generateTexture();
    }

    public void createCloudTexture() {
        textureEngine.prepareSurfaceForTexture(false);
        CloudGenerator cGen = new CloudGenerator(textureEngine.getBuffer());
        cGen.generate();
        texCloudId = textureEngine.generateTexture();
    }

    public abstract void render(boolean backGround);

    public void prepareTiles() {
        initialized = true;
        meshBACK.clearBuffers();
        meshFORE.clearBuffers();
    }

    public void updateTiles(Point cameraNew, Area[] p_areas, int compteur_animation) {
        if (meshBACK != null && cameraX != -1 && cameraY != -1) {
            meshBACK.startInitialization();
            meshFORE.startInitialization();
            int previousX = cameraX >> 4;
            int previousY = cameraY >> 4;
            int tileStartX = cameraNew.x >> 4;
            int tileStartY = cameraNew.y >> 4;
            int tileEndX = tileStartX + (Zildo.viewPortX >> 4);
            int tileEndY = tileStartY + (Zildo.viewPortY >> 4);
            if (previousX != tileStartX || previousY != tileStartY) {
                meshBACK.initFreeBuffer(cameraNew);
                meshFORE.initFreeBuffer(cameraNew);
            }
            for (Area theMap : p_areas) {
                if (theMap == null) {
                    break;
                }
                Point offset = theMap.getOffset();
                boolean yOut = false;
                boolean xOut = false;
                int sizeX = theMap.getDim_x();
                int sizeY = theMap.getDim_y();
                int dx = sizeX;
                int dy = sizeY;
                if (offset.x > 0) {
                    dx = offset.x >> 4;
                }
                if (offset.y > 0) {
                    dy = offset.y >> 4;
                }
                for (int ay = 0; ay < sizeY; ay++) {
                    int y = ay + (offset.y >> 4);
                    yOut = (y < tileStartY || y > tileEndY + 1);
                    if (yOut) {
                        continue;
                    }
                    for (int ax = 0; ax < sizeX; ax++) {
                        int x = ax + (offset.x >> 4);
                        xOut = x < tileStartX || x > tileEndX;
                        if (xOut) {
                            continue;
                        }
                        int n_motif = 0, n_animated_motif;
                        Case mapCase = theMap.get_mapcase((x + dx) % dx, (y + dy) % dy + 4);
                        if (mapCase != null) {
                            boolean changed = mapCase.isModified();
                            Tile back = mapCase.getBackTile();
                            int bank = back.bank;
                            n_motif = back.index;
                            n_animated_motif = mapCase.getAnimatedMotif(compteur_animation);
                            if (n_animated_motif != n_motif) {
                                changed = true;
                                n_motif = n_animated_motif;
                            }
                            meshBACK.updateTile(bank, x, y, n_motif, back.reverse, changed);
                            Tile back2 = mapCase.getBackTile2();
                            if (back2 != null) {
                                n_motif = back2.index;
                                meshBACK.updateTile(back2.bank, x, y, n_motif, back2.reverse, changed);
                            }
                            Tile fore = mapCase.getForeTile();
                            if (fore != null) {
                                n_motif = fore.index;
                                meshFORE.updateTile(fore.bank, x, y, fore.index, fore.reverse, changed);
                            }
                            mapCase.setModified(false);
                        }
                    }
                }
            }
            meshBACK.endInitialization();
            meshFORE.endInitialization();
        }
        cameraX = cameraNew.x;
        cameraY = cameraNew.y;
    }

    /**
	 * Return the bank's index in loaded ones from a given name
	 * 
	 * @param p_name
	 * @return int
	 */
    public static int getBankFromName(String p_name) {
        int i = 0;
        for (String s : tileBankNames) {
            if (s.equalsIgnoreCase(p_name)) {
                return i;
            }
            i++;
        }
        throw new RuntimeException("Bank " + p_name + " doesn't exist.");
    }

    /**
	 * Return the bank's name by index
	 * 
	 * @param nBank
	 * @return String
	 */
    public String getBankNameFromInt(int nBank) {
        return tileBankNames[nBank];
    }
}
