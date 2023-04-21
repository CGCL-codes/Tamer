package alertControl;

import xmlTools.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * AlertControlContainerExt - < . >
 *
 * @author J. Villalta (C)
 * @version 0506.libs
 *
 * Copyright (C) 2009  J. Villalta.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
public class AlertControlContainerExt extends AlertControlContainer implements xmlCompatible {

    public AlertControlContainerExt() {
        super();
    }

    public AlertControlContainerExt(Node xmlNode) throws MalformedXMLNode {
        super();
        try {
            NodeList alertList = xmlNode.getChildNodes();
            for (int i = 0; i < alertList.getLength(); i++) {
                Node n = alertList.item(i);
                if (n instanceof Element) {
                    AlertControl alert = new AlertControlExt(n);
                    add(alert);
                }
            }
        } catch (Exception e) {
            throw new MalformedXMLNode();
        }
    }

    @Override
    public Node saveAttributes() throws MalformedXMLNode {
        Document doc;
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            doc = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            return null;
        }
        Element alertsNode = doc.createElement("ALERTS");
        for (int i = 0; i < size(); i++) {
            Node n = ((AlertControlExt) get(i)).saveAttributes();
            n = doc.importNode(n, true);
            alertsNode.appendChild(n);
        }
        return alertsNode;
    }
}
