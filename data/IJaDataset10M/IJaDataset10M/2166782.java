package xml;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import model.Stu_Info_Model;
import view.St_Login_View;

/**
 *
 * @author 马凯
 */
public class St_Info_XML {

    private static St_Info_XML six;

    private String path;

    private Stu_Info_Model sim;

    private St_Login_View slv;

    private String attend_Num;

    private List stuName;

    private List stuID;

    private List stuCID;

    private List stuAttend;

    public static St_Info_XML getInstance(St_Login_View slv) {
        if (six == null) {
            six = new St_Info_XML();
            six.path = slv.getStuModel().getSub_ID();
            six.slv = slv;
        }
        return six;
    }

    public void run() {
        try {
            System.out.println("The path is " + this.path + ".xml");
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(this.path + ".xml")));
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(this.path + ".xml", true)));
            while ((sim = (Stu_Info_Model) decoder.readObject()) != null) {
                this.stuCID = sim.getCid();
                this.stuID = sim.getStu_ID();
                this.stuName = sim.getName();
                this.stuAttend = sim.getAttend_Num();
                for (int i = 0; i < stuCID.size(); i++) {
                    if (this.stuCID.get(i).equals(slv.getStuModel().getCid_TF().getText())) {
                        slv.getStuModel().getName_TF().setText(this.stuName.get(i).toString());
                        slv.getStuModel().getStu_TF().setText(this.stuID.get(i).toString());
                        slv.getStuModel().getAttend_TF().setText(this.stuAttend.get(i).toString());
                        int f = Integer.valueOf(this.stuAttend.get(i).toString()) + 1;
                        this.stuAttend.set(i, f);
                    }
                }
            }
            encoder.close();
            decoder.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
