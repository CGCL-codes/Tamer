package gui.editing.lsystems;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import models.ILoadListener;
import models.ModelServer;
import models.O3dFile;
import models.ObjectParser;
import models.VisibleObject;

public class TextureTab extends JPanel implements ILoadListener {

    protected O3dFile target;

    protected File curModelFile;

    protected List<File> texFiles;

    protected JButton saveO3d;

    private static final long serialVersionUID = 1L;

    public TextureTab() {
        texFiles = new LinkedList<File>();
        ObjectParser.addLoadListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        saveO3d = new JButton("save in o3d");
        saveO3d.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    saveInO3d(curModelFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    protected void saveInO3d(File dst) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new FileReader(dst));
        for (int i = 0; i < 3; i++) sb.append(r.readLine() + "\n");
        for (Component c : getComponents()) {
            if (!(c instanceof TextureSwitcher)) continue;
            sb.append("tt ");
            sb.append(((TextureSwitcher) c).texLabel.getText());
            sb.append(" ");
        }
        sb.append("\n");
        r.readLine();
        String line;
        while ((line = r.readLine()) != null) sb.append(line + "\n");
        r.close();
        FileWriter w = new FileWriter(dst);
        w.write(sb.toString());
        w.close();
    }

    public void update(O3dFile o3dFile) {
        curModelFile = o3dFile;
        target = o3dFile;
        VisibleObject o = ModelServer.getModel(target);
        if (o == null) return;
        removeAll();
        List<String> texFiles = getTexNames();
        for (int i = 0; i < o.textures.size(); i++) {
            add(new TextureSwitcher(o.textures.get(i), i, texFiles.get(i)));
        }
        add(saveO3d);
    }

    public void update(GLAutoDrawable gld) {
        for (Component c : getComponents()) {
            if (!(c instanceof TextureSwitcher)) continue;
            TextureSwitcher ts = (TextureSwitcher) c;
            if (ts.update != null) ;
            ts.updateTex(gld);
        }
    }

    public O3dFile getModelFile() {
        return target;
    }

    public void loadFinished(VisibleObject o) {
        if (o.o3dFile.equals(target)) update(o.o3dFile);
    }

    public void loadCancelled() {
    }

    public void loadStarted(File f) {
    }

    protected List<String> getTexNames() {
        List<String> ret = new LinkedList<String>();
        try {
            Scanner s = new Scanner(curModelFile);
            s.nextLine();
            s.nextLine();
            s.nextLine();
            Scanner texLine = new Scanner(s.nextLine());
            while (texLine.hasNext()) {
                ret.add(texLine.next());
            }
            texLine.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
