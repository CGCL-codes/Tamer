package org.jude.client.editor.swing.logical.relation;

import java.awt.datatransfer.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.io.*;
import org.jude.client.*;
import org.jude.client.logger.*;
import org.jude.client.db.*;
import org.jude.client.db.kb.*;
import org.jude.client.content.*;
import org.jude.client.editor.*;
import org.jude.client.dataflavor.*;
import org.jude.simplelogic.*;
import org.jude.client.editor.swing.*;
import org.jude.client.editor.swing.logical.*;

/**
 *  <p>
 *
 *  View and edit every relation using a table rapresentation.
 *
 *@author     Massimo Zaniboni
 *@version    $Revision: 1.2 $
 */
public class TableBasedRelationViewer extends TableBasedRelationEditor {

    public static String editorDescription = "Display the values of a relation using a table.";

    public static JudeObject editorCategory = Editor.SWING_EDITOR;

    public static Class javaContentClass = RelationWithArgumentAtPosition.class;

    public static JudeObject editorType = Editor.VIEWER;

    protected SwingEditor getWrappedEditor(QuerySolutions solutions) {
        QuerySolutionsViewer editor = new QuerySolutionsViewer();
        editor.setContent(solutions);
        return editor;
    }

    public TableBasedRelationViewer() {
        super();
    }

    public TableBasedRelationViewer(JudeObject mode) {
        super(mode);
    }
}
