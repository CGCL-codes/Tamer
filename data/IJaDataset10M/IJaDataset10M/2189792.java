package com.maigc.jawin;

import org.jawin.DispatchPtr;
import org.jawin.win32.Ole32;

/**
 * Creates a simple PowerPoint presentation
 *
 * @version     $Revision: 1.4 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class CreatePpt {

    public static void main(String[] args) {
        try {
            Ole32.CoInitialize();
            DispatchPtr app = new DispatchPtr("PowerPoint.Application");
            app.put("Visible", true);
            DispatchPtr preses = (DispatchPtr) app.get("Presentations");
            DispatchPtr pres = (DispatchPtr) preses.invoke("add", new Integer(-1));
            DispatchPtr slides = (DispatchPtr) pres.get("Slides");
            DispatchPtr slide = (DispatchPtr) slides.invoke("Add", new Integer(1), new Integer(2));
            DispatchPtr shapes = (DispatchPtr) slide.get("Shapes");
            DispatchPtr shape = (DispatchPtr) shapes.invoke("Item", new Integer(1));
            DispatchPtr frame = (DispatchPtr) shape.get("TextFrame");
            DispatchPtr range = (DispatchPtr) frame.get("TextRange");
            range.put("Text", "Use Jawin to call COM objects");
            Ole32.CoUninitialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
