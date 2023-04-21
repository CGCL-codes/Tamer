package com.objectdraw.server.tools;

import com.objectdraw.server.ObjectDrawServer;
import com.objectdraw.server.data.Action;
import com.objectdraw.server.tools.data.DrawingObject;

/**
 * Used to create server-side text objects.
 * 
 * @author Harrison
 *
 */
public class TextServerTool extends ServerTool {

    private DrawingObject dro = new DrawingObject();

    /**
	 * Initailize the tool
	 */
    public TextServerTool() {
        super();
        name = "key";
        dro.setColor(ObjectDrawServer.getInstance().getCurrentColor());
        dro.setTool(name);
    }

    @Override
    public void addAction(Action act) {
        if (act != null) {
            if ("key".equalsIgnoreCase(act.getAction())) {
                if ("pressed".equalsIgnoreCase(act.getEvent())) {
                    dro.setText(dro.getText() + act.getArguments());
                }
            }
            if ("mouse".equalsIgnoreCase(act.getAction())) {
                if ("pressed".equalsIgnoreCase(act.getEvent())) {
                    ObjectDrawServer serv = ObjectDrawServer.getInstance();
                    dro = new DrawingObject();
                    String[] cords = act.getArguments().split(" ");
                    dro.AddCoords(cords[0], cords[1]);
                    dro.setColor(serv.getCurrentColor());
                    dro.setTool(name);
                    serv.addObject(dro);
                }
            }
        }
    }
}
