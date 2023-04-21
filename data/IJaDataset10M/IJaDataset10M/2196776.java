package de.ios.kontor.cl.customer;

import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.util.Properties;
import de.ios.framework.gui.*;
import de.ios.framework.basic.*;
import de.ios.framework.remote.sv.co.*;
import de.ios.kontor.utils.*;
import de.ios.kontor.sv.main.co.*;
import de.ios.kontor.sv.address.co.*;

/**
 *
 *
 * @author ms.
 * @version $Id: LanguageEditView.java,v 1.1.1.1 2004/03/24 23:02:05 nanneb Exp $.
 */
public class LanguageEditView extends KontorView {

    public static final boolean debug = true;

    protected PositionLayout PL = null;

    protected KontorView parentView = null;

    protected Button okB;

    protected Button killB;

    protected Label nameL;

    protected StringField nameF = new StringField();

    protected Label shortNameL;

    protected StringField shortNameF = new StringField();

    protected RLanguage data;

    /**
   * constructor
   *
   * @param session.
   * @param _parentView.
   * @param _data.
   */
    public LanguageEditView(KontorSession session, KontorView _parentView, RLanguage _data) {
        super(session);
        parentView = _parentView;
        data = _data;
        if (debug) Debug.println(Debug.INFO, this, "constructor...");
    }

    /**
   * constructor
   *
   * @param session.
   + @param properties.
   * @param _parentView.
   * @param _data.
   */
    public LanguageEditView(KontorSession session, Properties properties, KontorView _parentView, RLanguage _data) {
        super(session, properties);
        parentView = _parentView;
        data = _data;
        if (debug) Debug.println(Debug.INFO, this, "constructor...");
    }

    /**
   * "init" Applet emulation
   */
    public void kvInit() {
        if (debug) Debug.println(Debug.INFO, this, "init()...");
        super.kvInit();
        createDialog();
    }

    /**
   * "start" Applet emulation
   */
    public void kvStart() {
        if (debug) Debug.println(Debug.INFO, this, "start()...");
        super.kvStart();
        setData();
    }

    /**
   * "stop" Applet emulation
   */
    public void kvStop() {
        if (debug) Debug.println(Debug.INFO, this, "stop()...");
        super.kvStop();
    }

    /**
   * "destroy" Applet emulation
   */
    public void kvDestroy() {
        if (debug) Debug.println(Debug.INFO, this, "destroy()...");
        super.kvDestroy();
    }

    public void createDialog() {
        if (debug) Debug.println(Debug.INFO, this, "createDialog()...");
        okB = new Button(getDesc("add_button"));
        killB = new Button(getDesc("cancel"));
        nameL = new Label(getDesc("name"));
        shortNameL = new Label(getDesc("sname"));
        setLayout(new PositionLayout(5, 5, 8, 2, true));
        PL = (PositionLayout) getLayout();
        add("", okB);
        add("", killB);
        add("", nameL);
        add("", nameF);
        add("", shortNameL);
        add("", shortNameF);
        PL.defineAutoPositioning(nameL, null, null, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(nameF, nameL, null, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(shortNameL, null, nameL, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(shortNameF, shortNameL, nameF, null, null, -1, -1, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(okB, null, shortNameL, null, shortNameF, -1, 2, PL.NONE, PL.NONE);
        PL.defineAutoPositioning(killB, okB, null, null, null, -1, -1, PL.RIGHT, PL.NONE);
        okB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (data == null) data = getSession().getLanguageController().createLanguage();
                    data.setName(nameF.getValue());
                    data.setShortName(shortNameF.getValue());
                    data.store();
                } catch (Exception E) {
                    ExceptionDialog.show(getDesc("systemerror"), getDesc("err_save_data"), E);
                }
                if (parentView != null) {
                    try {
                        parentView.setData();
                    } catch (Exception E) {
                        ExceptionDialog.show(getDesc("systemerror"), getDesc("err_disp"), E);
                    }
                }
                doDestroy();
            }
        });
        killB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doDestroy();
            }
        });
    }

    public void setData() {
        if (data != null) {
            try {
                nameF.setValue(data.getName());
                shortNameF.setValue(data.getShortName());
            } catch (Exception e) {
                ExceptionDialog.show(getDesc("systemerror"), getDesc("err_save_data"), e);
            }
        }
    }

    public void getData() {
    }

    public String getViewName() {
        return getViewName(getDesc("lang") + " " + (data == null ? getDesc("add") : getDesc("modify")));
    }
}
