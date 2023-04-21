package org.doot.conf;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author tapan
 */
public class SaveAction extends AbstractAction {

    DootConf conf;

    public SaveAction(DootConf t) {
        super("Save");
        conf = t;
    }

    public void actionPerformed(ActionEvent arg0) {
        configurator config = new configurator();
        config.setHost(conf.getHost());
        config.setUser(conf.getUser());
        config.setPass(conf.getPass());
        config.setDBName(conf.getDBName());
        config.setDataDir(conf.getDataDir());
        config.setDBType(conf.getDBType());
        if (conf.getConfig() == null) {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileHidingEnabled(false);
            int option = chooser.showSaveDialog(conf);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    JAXBContext ctx = JAXBContext.newInstance(configurator.class);
                    Marshaller m = ctx.createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    m.marshal(config, chooser.getSelectedFile());
                    conf.setConfig(chooser.getSelectedFile().getAbsolutePath());
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                JAXBContext ctx = JAXBContext.newInstance(configurator.class);
                Marshaller m = ctx.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                m.marshal(config, new File(conf.getConfig()));
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}
