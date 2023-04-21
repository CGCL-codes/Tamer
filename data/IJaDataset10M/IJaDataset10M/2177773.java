package at.redeye.MSGViewer;

import java.io.File;
import org.apache.log4j.Level;
import at.redeye.FrameWork.base.BaseModuleLauncher;
import at.redeye.FrameWork.base.Root;
import at.redeye.MSGViewer.factory.MessageParserFactory;
import com.auxilii.msgparser.Message;

/**
 * 
 * @author martin
 */
public class Msg2MBox {

    ModuleLauncher module_launcher;

    Root root;

    public Msg2MBox(ModuleLauncher module_launcher) {
        this.module_launcher = module_launcher;
        root = module_launcher.root;
        BaseModuleLauncher.BaseConfigureLogging(Level.ERROR);
    }

    public void work() {
        boolean converted = false;
        MessageParserFactory factory = new MessageParserFactory();
        for (String arg : module_launcher.args) {
            if (arg.toLowerCase().endsWith(".msg")) {
                converted = true;
                try {
                    Message msg = factory.parseMessage(new File(arg));
                    int idx = arg.lastIndexOf(".");
                    File mbox_file = new File(arg.substring(0, idx) + ".mbox");
                    factory.saveMessage(msg, mbox_file);
                } catch (Exception ex) {
                    System.err.print(ex);
                    ex.printStackTrace();
                }
            }
        }
        if (!converted) usage();
    }

    public void usage() {
        System.out.println(root.MlM("usage: msg2mbox FILE FILE ...."));
    }
}
