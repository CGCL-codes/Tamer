package org.ubispotsim.xmlparser;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.ListIterator;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.ubispotsim.common.Constants;
import org.ubispotsim.common.DbOperation;
import org.ubispotsim.common.GUI;

public class LandmarkParser {

    public LandmarkParser() {
    }

    @SuppressWarnings("unchecked")
    public void startParsing(String xmlfile) throws SQLException, IOException {
        PreparedStatement prestmt;
        ResultSet rs;
        int frequency;
        try {
            SAXBuilder builder = new SAXBuilder();
            org.jdom.Document doc = builder.build(new File(xmlfile));
            Element root = doc.getRootElement();
            List<Element> landmarkLst = root.getChildren();
            ListIterator<Element> landmarkLstIter = landmarkLst.listIterator();
            List<Element> tableLst;
            ListIterator<Element> tableLstIter;
            List<Element> entryLst;
            ListIterator<Element> entryLstIter;
            Element landmark, table, value;
            String elemID, name, parent, category, country, net, localarea, cell, wlanID, btID;
            float lon, lat;
            NumberFormat nf = NumberFormat.getInstance(Constants.INLOCALE);
            elemID = null;
            while (landmarkLstIter.hasNext()) {
                landmark = landmarkLstIter.next();
                tableLst = landmark.getChildren();
                tableLstIter = tableLst.listIterator();
                while (tableLstIter.hasNext()) {
                    table = tableLstIter.next();
                    entryLst = table.getChildren();
                    entryLstIter = entryLst.listIterator();
                    if (table.getName().equals("element")) {
                        name = parent = category = null;
                        while (entryLstIter.hasNext()) {
                            value = entryLstIter.next();
                            if (value.getName().equals("id")) elemID = value.getTextTrim(); else if (value.getName().equals("name")) name = value.getTextTrim(); else if (value.getName().equals("parent")) {
                                parent = value.getTextTrim();
                            } else if (value.getName().equals("category")) {
                                category = value.getTextTrim();
                            }
                        }
                        try {
                            prestmt = DbOperation.getConn().prepareStatement("SELECT * FROM element WHERE id=?");
                            prestmt.setString(1, elemID);
                            rs = (ResultSet) prestmt.executeQuery();
                            if (!rs.next()) {
                                prestmt = (PreparedStatement) DbOperation.getConn().prepareStatement("INSERT INTO element (id, name, parent, category) VALUES (?,?,?,?)");
                                prestmt.setString(1, elemID);
                                prestmt.setString(2, name);
                                prestmt.setString(3, parent);
                                prestmt.setString(4, category);
                                prestmt.executeUpdate();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            DbOperation.closeConn();
                        }
                    } else if (table.getName().equals("gps")) {
                        lat = lon = 0;
                        while (entryLstIter.hasNext()) {
                            value = entryLstIter.next();
                            if (value.getName().equals("long")) lon = nf.parse(value.getTextTrim()).floatValue(); else if (value.getName().equals("lat")) lat = nf.parse(value.getTextTrim()).floatValue();
                        }
                        try {
                            prestmt = (PreparedStatement) DbOperation.getConn().prepareStatement("INSERT INTO assign_gps (elemID, lon, lat) VALUES (?,?,?)");
                            prestmt.setString(1, elemID);
                            prestmt.setFloat(2, lon);
                            prestmt.setFloat(3, lat);
                            prestmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            DbOperation.closeConn();
                        }
                    } else if (table.getName().equals("gsm")) {
                        country = net = localarea = cell = null;
                        frequency = 0;
                        while (entryLstIter.hasNext()) {
                            value = entryLstIter.next();
                            if (value.getName().equals("cellId")) cell = value.getTextTrim(); else if (value.getName().equals("laId")) localarea = value.getTextTrim(); else if (value.getName().equals("cid")) {
                                country = value.getTextTrim();
                            } else if (value.getName().equals("net")) {
                                net = value.getTextTrim();
                            } else if (value.getName().equals("frequency")) {
                                frequency = Integer.parseInt(value.getTextTrim());
                            }
                        }
                        try {
                            prestmt = (PreparedStatement) DbOperation.getConn().prepareStatement("INSERT INTO assign_gsm (elemID, country, net, localarea, cell, frequency) VALUES (?,?,?,?,?,?)");
                            prestmt.setString(1, elemID);
                            prestmt.setString(2, country);
                            prestmt.setString(3, net);
                            prestmt.setString(4, localarea);
                            prestmt.setString(5, cell);
                            prestmt.setInt(6, frequency);
                            prestmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            GUI.setMonitor("Error in assign_gsm: " + elemID + "  " + cell + "  " + localarea + "\n");
                        } finally {
                            DbOperation.closeConn();
                        }
                    } else if (table.getName().equals("wlan")) {
                        wlanID = null;
                        frequency = 0;
                        while (entryLstIter.hasNext()) {
                            value = entryLstIter.next();
                            if (value.getName().equals("id")) {
                                wlanID = value.getTextTrim();
                            } else if (value.getName().equals("frequency")) {
                                frequency = Integer.parseInt(value.getTextTrim());
                            }
                        }
                        try {
                            prestmt = (PreparedStatement) DbOperation.getConn().prepareStatement("INSERT INTO assign_wlan (elemID, wlanID, frequency) VALUES (?,?,?)");
                            prestmt.setString(1, elemID);
                            prestmt.setString(2, wlanID);
                            prestmt.setInt(3, frequency);
                            prestmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            DbOperation.closeConn();
                        }
                    } else if (table.getName().equals("bluetooth")) {
                        btID = null;
                        frequency = 0;
                        while (entryLstIter.hasNext()) {
                            value = entryLstIter.next();
                            if (value.getName().equals("id")) btID = value.getTextTrim(); else if (value.getName().equals("frequency")) frequency = Integer.parseInt(value.getTextTrim());
                        }
                        try {
                            prestmt = (PreparedStatement) DbOperation.getConn().prepareStatement("INSERT INTO assign_bluetooth (elemID, btID, frequency) VALUES (?,?,?)");
                            prestmt.setString(1, elemID);
                            prestmt.setString(2, btID);
                            prestmt.setInt(3, frequency);
                            prestmt.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            DbOperation.closeConn();
                        }
                    }
                }
            }
            GUI.setMonitor("Parsing landmark fingerprints finished!");
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            DbOperation.closeConn();
        }
    }
}
