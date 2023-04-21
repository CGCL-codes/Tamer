package org.jato.debug;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.jato.*;
import org.jato.debug.*;
import org.jato.tags.*;
import org.jdom.*;

public class DebugTreeModel extends DefaultTreeModel {

    public DebugTreeModel(DebugTreeNode root) {
        super(root);
    }
}
