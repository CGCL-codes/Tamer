package wotlas.common.character;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import wotlas.common.ImageLibRef;
import wotlas.common.Player;
import wotlas.common.objects.inventories.AielWarriorInventory;
import wotlas.common.objects.inventories.Inventory;
import wotlas.common.universe.WotlasLocation;
import wotlas.libs.graphics2d.Drawable;
import wotlas.libs.graphics2d.ImageIdentifier;
import wotlas.libs.graphics2d.drawable.AuraEffect;
import wotlas.libs.graphics2d.drawable.ShadowSprite;
import wotlas.libs.graphics2d.drawable.Sprite;
import wotlas.libs.graphics2d.drawable.SpriteDataSupplier;
import wotlas.libs.graphics2d.filter.ColorImageFilter;

/** An Aiel Warrior character.
 *
 * @author Aldiss, Elann
 * @see wotlas.common.character.Male
 */
public class AielWarrior extends Male {

    /** Aiel rank
     */
    public static final String aielRank[][] = { { "Aiel Warrior", "warrior-0" } };

    /** Aiel rank
     */
    public static final Color aielColor[] = { new Color(180, 170, 90) };

    /** Aiel status ( warrior, ... ). [PUBLIC INFO]
     */
    private String characterRank;

    /** Current Sprite.
     */
    private transient Sprite aielSprite;

    /** Current Shadow.
     */
    private transient ShadowSprite aielShadowSprite;

    /** Current Aura.
     */
    private transient AuraEffect aielAuraEffect;

    /** ColorImageFilter for InteriorMap Sprites.
     */
    private transient ColorImageFilter filter;

    /** Constructor
     */
    public AielWarrior() {
    }

    /** To get a Drawable for this character. This should not be used on the
     *  server side.
     *
     *  The returned Drawable is unique : we always return the same drawable per
     *  AesSedai instance.
     *
     * @param player the player to chain the drawable to. If a XXXDataSupplier is needed
     *               we sets it to this player object.
     * @return a Drawable for this character
     */
    public Drawable getDrawable(Player player) {
        if (this.aielSprite != null) return this.aielSprite;
        this.aielSprite = new Sprite((SpriteDataSupplier) player, ImageLibRef.PLAYER_PRIORITY);
        this.aielSprite.useAntialiasing(true);
        updateColorFilter();
        return this.aielSprite;
    }

    /** Updates the color filter that is used for the AesSedai sprite.
     */
    private void updateColorFilter() {
        if (this.aielSprite == null) return;
    }

    /** Return the character's shadow. Important: a character Drawable MUST have been created
     *  previously ( via a getDrawable call ). You don't want to create a shadow with no
     *  character, do you ?
     *
     *  @return character's Shadow Drawable.
     */
    public Drawable getShadow() {
        if (this.aielShadowSprite != null) return this.aielShadowSprite;
        String path = null;
        path = "players-0/shadows-3/aiel-w-walking-7";
        this.aielShadowSprite = new ShadowSprite(this.aielSprite.getDataSupplier(), new ImageIdentifier(path), ImageLibRef.SHADOW_PRIORITY, 4, 4);
        return this.aielShadowSprite;
    }

    /** Return the character's aura.
     *  @return character's Aura Drawable.
     */
    public Drawable getAura() {
        if (this.aielAuraEffect != null) {
            if (this.aielAuraEffect.isLive()) {
                return null;
            }
            this.aielAuraEffect.reset();
            return this.aielAuraEffect;
        }
        this.aielAuraEffect = new AuraEffect(this.aielSprite.getDataSupplier(), getAuraImage(), ImageLibRef.AURA_PRIORITY, 5000);
        this.aielAuraEffect.useAntialiasing(true);
        this.aielAuraEffect.setAuraMaxAlpha(0.5f);
        return this.aielAuraEffect;
    }

    /** To get the Aura Image Identifier.
     */
    private ImageIdentifier getAuraImage() {
        String symbolName = null;
        for (int i = 0; i < AielWarrior.aielRank.length; i++) if (this.characterRank.equals(AielWarrior.aielRank[i][0])) {
            symbolName = AielWarrior.aielRank[i][1];
            break;
        }
        if (symbolName == null) symbolName = AielWarrior.aielRank[0][1];
        return new ImageIdentifier("players-0/symbols-2/aiel-symbols-5/" + symbolName + ".gif");
    }

    /** Return the character's color.
     *  @return character's color
     */
    public Color getColor() {
        for (int i = 0; i < AielWarrior.aielRank.length; i++) if (this.characterRank.equals(AielWarrior.aielRank[i][0])) return AielWarrior.aielColor[i];
        return Color.black;
    }

    /** To get the WotCharacter community name.
     * @return the name of the community.
     */
    public String getCommunityName() {
        return "Aiel";
    }

    /** To get the rank of this WotCharacter in his/her community.
     * @return the rank of this wotcharacter in his/her community.
     */
    public String getCharacterRank() {
        return this.characterRank;
    }

    /** To set the rank of this WotCharacter in his/her community.
     *  IMPORTANT : if the rank doesnot exist it is  set to "unknown".
     *
     * @param rank the rank of this wotcharacter in his/her community.
     */
    public void setCharacterRank(String rank) {
        if (rank != null) for (int i = 0; i < AielWarrior.aielRank.length; i++) if (rank.equals(AielWarrior.aielRank[i][0])) {
            this.characterRank = rank;
            return;
        }
        this.characterRank = "unknown";
    }

    /** Returns an image for this character.
     *
     *  @param playerLocation player current location
     *  @return image identifier of this character.
     */
    @Override
    public ImageIdentifier getImage(WotlasLocation playerLocation) {
        ImageIdentifier imID = super.getImage(playerLocation);
        if (imID == null) {
            if (this.aielSprite != null && this.filter != null) this.aielSprite.setDynamicImageFilter(this.filter);
            String path = null;
            path = "players-0/aiel-9/aiel-warrior-walking-0";
            return new ImageIdentifier(path);
        }
        if (this.aielSprite != null) this.aielSprite.setDynamicImageFilter(null);
        return imID;
    }

    /** Returns the fanfare sound of this character class.
     *  @return fanfare sound file name
     */
    public String getFanfareSound() {
        return "fanfare-aiel.wav";
    }

    /** To get a new Inventory for this WotCharacter.<br>
     * In this case, it is an AielWarriorInventory.
     * @return a new inventory for this char
     */
    public Inventory createInventory() {
        return new AielWarriorInventory();
    }

    /** To put the WotCharacter's data on the network stream. You don't need
      * to invoke this method yourself, it's done automatically.
      *
      * @param ostream data stream where to put your data (see java.io.DataOutputStream)
      * @param publicInfoOnly if false we write the player's full description, if true
      *                     we only write public info
      * @exception IOException if the stream has been closed or is corrupted.
      */
    @Override
    public void encode(DataOutputStream ostream, boolean publicInfoOnly) throws IOException {
        super.encode(ostream, publicInfoOnly);
        ostream.writeUTF(this.characterRank);
    }

    /** To retrieve your WotCharacter's data from the stream. You don't need
     * to invoke this method yourself, it's done automatically.
     *
     * @param istream data stream where you retrieve your data (see java.io.DataInputStream)
     * @param publicInfoOnly if false it means the available data is the player's full description,
     *                     if true it means we only have public info here.
     * @exception IOException if the stream has been closed or is corrupted.
     */
    @Override
    public void decode(DataInputStream istream, boolean publicInfoOnly) throws IOException {
        super.decode(istream, publicInfoOnly);
        this.characterRank = istream.readUTF();
    }
}
