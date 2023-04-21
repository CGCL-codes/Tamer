package org.rapla.gui;

import java.awt.Component;
import org.rapla.entities.Entity;
import org.rapla.entities.RaplaObject;
import org.rapla.framework.RaplaException;

public interface EditController {

    public static final String ROLE = EditController.class.getName();

    EditComponent createUI(RaplaObject obj) throws RaplaException;

    void edit(Entity obj, Component owner) throws RaplaException;

    void edit(Entity obj, String title, Component owner) throws RaplaException;
}
