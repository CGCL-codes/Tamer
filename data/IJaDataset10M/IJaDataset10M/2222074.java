package com.peterhi.server.handlers;

import java.util.Date;
import org.prevayler.Transaction;
import com.peterhi.persist.beans.Root;
import com.peterhi.persist.beans.Classroom;
import com.peterhi.persist.beans.shapes.Shape;

class AddShape implements Transaction {

    private String channel;

    private Shape shape;

    public AddShape(String channel, Shape shape) {
        this.channel = channel;
        this.shape = shape;
    }

    public void executeOn(Object o, Date ts) {
        Root root = (Root) o;
        Classroom classroom = root.getClassroom(channel);
        if (classroom == null) {
            return;
        }
        classroom.addShape((Shape) shape.clone());
    }
}
