package es.org.chemi.games.sokoban.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.zip.ZipFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import es.org.chemi.games.sokoban.SokobanMessages;
import es.org.chemi.games.sokoban.SokobanPlugin;
import es.org.chemi.games.sokoban.events.MoveHelper;
import es.org.chemi.games.sokoban.ui.TileMap;

public class DemoAction extends Action {

    private TileMap map = null;

    public DemoAction(String label, TileMap map) {
        super(label);
        this.map = map;
    }

    public void run() {
        SokobanPlugin.trace(this.getClass().getName(), "Demo game creation solicited.");
        try {
            ZipFile zf = new ZipFile(new File(FileLocator.getBundleFile(SokobanPlugin.getDefault().getBundle()), "levels/" + map.getPreferences().getMode() + ".map"));
            BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry("" + map.getPreferences().getLevel(map.getPreferences().getMode()) + ".soa"))));
            map.setIsNewLevel(true);
            map.loadMap();
            map.redraw();
            map.setFocus();
            String answer = br.readLine();
            br.close();
            zf.close();
            map.setDemo(true);
            new MoveHelper(map).move(answer);
        } catch (Exception e) {
            map.setDemo(false);
            MessageDialog.openInformation(map.getShell(), "No Answer", SokobanMessages.getString("DemoAction.noAnswer"));
        }
    }
}
