package com.mainatom.ui;

import com.mainatom.tab.*;
import com.mainatom.testutils.*;
import com.mainatom.ui.editors.*;

public class AEditorTest extends UITestCase {

    public static class Tab1 extends TabId {

        protected void onInit() throws Exception {
            super.onInit();
            TableBuilder tb = new TableBuilder(this);
            tb.field("name", TabSysDomain.STRING, 20, "Наименование");
            tb.field("value", TabSysDomain.STRING, 20, "Значение");
        }

        public void fill1() {
            clearData();
            for (int i = 0; i < 10; i++) {
                append();
                char c = (char) (97 + i);
                f("name").setAsString("" + c + "n" + i);
                f("value").setAsString("v" + i);
            }
            first();
        }
    }

    public void test1() throws Exception {
        AFrame f = new AFrame();
        final Tab1 t = new Tab1();
        f.setTable("default", t);
        t.fill1();
        CnEditor ed = new CnEditor();
        ed.setField("name");
        f.addControl(ed);
        ed = new CnEditor();
        ed.setField("value");
        f.addControl(ed);
        f.addControl(ui.button("ok"));
        ui.showFrame(f, new IRobotFrameRunner() {

            public void exec(RobotFrame r) throws Exception {
                r.keypress("tab");
                r.screenShot();
                r.textpress("qws");
                r.screenShot();
                r.keypress("up");
                r.textpress("444z");
                r.screenShot();
                assertEquals(t.f("name").getAsString(), "444z");
                assertEquals(t.f("value").getAsString(), "qws");
            }
        });
    }
}
