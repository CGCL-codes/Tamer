package org.jboke.xul.factories;

import java.awt.*;
import javax.swing.JTextPane;
import org.xml.sax.Attributes;
import org.jboke.xul.XULBuilder;

/**
 * The html-tag builder
 *
 * @author Kurt Huwig
 * @version $Id: XULHTML.java,v 1.3 2001/04/29 14:15:07 kurti Exp $
 *
 * This file is part of the JBoKe-Project, see http://www.jboke.org/
 * (c) 2001 iKu Netzwerkl&ouml;sungen
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
public class XULHTML extends XULBuilder {

    public static XULBuilder getInstance() {
        return new XULHTML();
    }

    private JTextPane jtp;

    protected void setUp() {
        jtp = new JTextPane();
        jtp.setContentType("text/html");
    }

    protected void parseAttribute(String sName, String sValue) {
    }

    private StringBuffer sb = new StringBuffer();

    public void addCharacters(char[] ac, int iStart, int iLength) {
        sb.append(ac, iStart, iLength);
    }

    public Object[] getElements() {
        jtp.setText(sb.toString());
        return new Component[] { jtp };
    }
}
