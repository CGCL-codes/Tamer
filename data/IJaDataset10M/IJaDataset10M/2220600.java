package org.twdata.kokua.tw.gui.chat;

import org.swixml.SwingEngine;
import org.twdata.kokua.signal.*;
import org.twdata.kokua.*;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.*;
import java.util.*;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.datatransfer.Clipboard;
import org.swixml.*;
import java.awt.Dimension;
import org.apache.log4j.Logger;
import java.awt.BorderLayout;
import org.twdata.kokua.tw.signal.*;
import org.twdata.kokua.tw.data.*;
import org.twdata.kokua.tw.model.*;
import org.twdata.kokua.tw.*;
import org.twdata.kokua.data.DaoManager;
import org.apache.log4j.Logger;

/**
 *@created    October 18, 2003
 */
public interface Printer {

    public void print(String msg);
}
