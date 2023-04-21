package com.cell.bms.game;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.cell.CObject;
import com.cell.bms.BMSFile;
import com.cell.bms.BMSPlayer;
import com.cell.bms.BMSFile.LoadingListener;
import com.cell.j2se.CStorage;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.Stage;
import com.g2d.display.ui.Button;
import com.g2d.display.ui.Form;
import com.g2d.display.ui.UIComponent;
import com.g2d.display.ui.event.ActionEvent;
import com.g2d.display.ui.event.ActionListener;

public class StageTitle extends Stage {

    public StageTitle() {
        setSize(Config.STAGE_WIDTH, Config.STAGE_HEIGHT);
    }

    public void added(DisplayObjectContainer parent) {
        new TitleForm().show(this);
    }

    public void removed(DisplayObjectContainer parent) {
    }

    public void update() {
    }

    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    class TitleForm extends Form implements ActionListener {

        Button btn_load = new Button("打开BMS", 200, 20);

        public TitleForm() {
            super.setCloseEnable(false);
            super.setSize(400, 400);
            super.addComponent(btn_load, 10, 10);
            btn_load.addEventListener(this);
        }

        @Override
        public void itemAction(UIComponent item, ActionEvent event) {
            if (item == btn_load) {
                File last = null;
                try {
                    byte[] last_loaded = CObject.getStorage().load("last_loaded", 0);
                    File last_file = new File(new String(last_loaded, "UTF-8"));
                    if (last_file.exists()) {
                        last = last_file.getParentFile();
                    }
                } catch (Exception err) {
                }
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("BMS File", "bms");
                fc.setFileFilter(filter);
                fc.setCurrentDirectory(last);
                fc.showOpenDialog(null);
                File file = fc.getSelectedFile();
                if (file != null) {
                    getRoot().changeStage(StageLoading.class, file.getPath(), StageGame.class);
                    try {
                        CObject.getStorage().save("last_loaded", 0, file.getPath().getBytes("UTF-8"));
                    } catch (Exception err) {
                    }
                }
            }
        }
    }
}
