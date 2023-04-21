package eu.livotov.tpt.demo.widgets;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import eu.livotov.tpt.TPTApplication;
import eu.livotov.tpt.demo.api.DemoItem;
import eu.livotov.tpt.gui.widgets.TPTCaptcha;
import eu.livotov.tpt.i18n.TM;
import java.io.Serializable;

/**
 *
 * @author dll
 */
public class CaptchaDemoItem implements DemoItem, Serializable {

    public boolean hasSourceCode() {
        return true;
    }

    public boolean hasShowCase() {
        return true;
    }

    public String getItemName() {
        return TM.get("cap.title");
    }

    public String getItemDescription() {
        return TM.get("cap.info");
    }

    public String getItemSourceCode() {
        return "public class CaptchaDemoWindow extends Window\n" + "    {\n" + "\n" + "        private TPTCaptcha captcha = new TPTCaptcha ();\n" + "        private VerticalLayout root = new VerticalLayout ();\n" + "\n" + "        public CaptchaDemoWindow ()\n" + "        {\n" + "            super ( \"Captcha Demo\" );\n" + "            initUI ();\n" + "        }\n" + "\n" + "        private void initUI ()\n" + "        {\n" + "            setSizeFull ();\n" + "            root.setSizeFull ();\n" + "            root.setMargin ( true );\n" + "            root.setSpacing ( true );\n" + "            setContent ( root );\n" + "\n" + "            Button btnChangeCode = new Button ( \"Change code\" );\n" + "            btnChangeCode.setStyleName ( Button.STYLE_LINK );\n" + "\n" + "            addComponent ( captcha );\n" + "            addComponent ( btnChangeCode );\n" + "\n" + "            root.setComponentAlignment ( captcha, Alignment.MIDDLE_CENTER );\n" + "            root.setComponentAlignment ( btnChangeCode, Alignment.MIDDLE_CENTER );\n" + "\n" + "            btnChangeCode.addListener ( new Button.ClickListener ()\n" + "            {\n" + "\n" + "                public void buttonClick ( ClickEvent event )\n" + "                {\n" + "                    captcha.generateCaptchaCode ( 5 );\n" + "                }\n" + "            } );\n" + "        }\n" + "    }";
    }

    public void performShowCase() {
        CaptchaDemoWindow w = new CaptchaDemoWindow();
        w.setWidth("400px");
        w.setHeight("200px");
        w.setModal(true);
        TPTApplication.getCurrentApplication().getMainWindow().addWindow(w);
    }

    public Resource getIcon() {
        return new ThemeResource("icons/captcha.png");
    }

    public class CaptchaDemoWindow extends Window {

        private TPTCaptcha captcha = new TPTCaptcha();

        private VerticalLayout root = new VerticalLayout();

        public CaptchaDemoWindow() {
            super(TM.get("cap.caption"));
            initUI();
        }

        private void initUI() {
            setSizeFull();
            root.setSizeFull();
            root.setMargin(true);
            root.setSpacing(true);
            setContent(root);
            Button btnChangeCode = new Button(TM.get("cap.change"));
            btnChangeCode.setStyleName(Button.STYLE_LINK);
            addComponent(captcha);
            addComponent(btnChangeCode);
            root.setComponentAlignment(captcha, Alignment.MIDDLE_CENTER);
            root.setComponentAlignment(btnChangeCode, Alignment.MIDDLE_CENTER);
            btnChangeCode.addListener(new Button.ClickListener() {

                public void buttonClick(ClickEvent event) {
                    captcha.generateCaptchaCode(5);
                }
            });
        }
    }
}
