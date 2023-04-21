package org.bibnet.playground;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.grlea.log.SimpleLogger;

public class RudolfinerRecord {

    private static final SimpleLogger LOG = new SimpleLogger(RudolfinerRecord.class);

    private Long id;

    private String jahr;

    private String autorVorname;

    private String autorNachname;

    private String autorGanz;

    private String autoren;

    private String haupttitel;

    private String untertitel;

    private String quelle;

    private String issn;

    private String jahrheft;

    private String volume = "";

    private String issue = "";

    private String seiten;

    private String sprache = "";

    private String beschreibung;

    private List<String> nebeneintragungen;

    private List<String> schlagworte;

    public List<RudolfinerRecord> getAllRecords(final boolean ohneDubletten, final Connection cn) {
        final List<RudolfinerRecord> list = new ArrayList<RudolfinerRecord>();
        String mysql = "SELECT * FROM artikel";
        if (ohneDubletten) {
            mysql = mysql + " WHERE `dublette` =0";
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement(mysql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final RudolfinerRecord rc = new RudolfinerRecord();
                rc.setId(rs.getLong("p_artikel"));
                rc.setJahr(rs.getString("jahr"));
                rc.setAutorVorname(rs.getString("a_vname"));
                rc.setAutorNachname(rs.getString("a_nname"));
                rc.setAutoren(rs.getString("a_co"));
                rc.setNebeneintragungen(extractNebeneintragungen(rc.getAutoren()));
                rc.setHaupttitel(rs.getString("titel"));
                rc.setUntertitel(rs.getString("utitel"));
                rc.setQuelle(rs.getString("quelle"));
                rc.setIssn(rs.getString("issn"));
                rc.setJahrheft(rs.getString("jahrheft"));
                rc.setVolume(rs.getString("volume"));
                rc.setIssue(rs.getString("issue"));
                rc.setSeiten(rs.getString("seiten"));
                rc.setSprache(rs.getString("sprache"));
                rc.setBeschreibung(rs.getString("beschreibung"));
                rc.setAutorGanz(rs.getString("h_autor"));
                rc.setSchlagworte(getHeadings(rc.getId(), cn));
                list.add(rc);
            }
        } catch (final Exception e) {
            LOG.error("ArrayList<RudolfinerRecord> getAllRecords(Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
        return list;
    }

    public List<RudolfinerRecord> getDuplicates(final RudolfinerRecord dublette, final Connection cn) {
        final List<RudolfinerRecord> list = new ArrayList<RudolfinerRecord>();
        final String spage = extractSpage(dublette.getSeiten());
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM artikel WHERE issn = ? AND  jahr = ? " + "AND issue = ? AND (seiten LIKE '" + spage + "-%' OR seiten = ?)");
            pstmt.setString(1, dublette.getIssn());
            pstmt.setString(2, dublette.getJahr());
            pstmt.setString(3, dublette.getIssue());
            pstmt.setString(4, spage);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final RudolfinerRecord rc = new RudolfinerRecord();
                rc.setId(rs.getLong("p_artikel"));
                rc.setJahr(rs.getString("jahr"));
                rc.setAutorVorname(rs.getString("a_vname"));
                rc.setAutorNachname(rs.getString("a_nname"));
                rc.setAutoren(rs.getString("a_co"));
                rc.setNebeneintragungen(extractNebeneintragungen(rc.getAutoren()));
                rc.setHaupttitel(rs.getString("titel"));
                rc.setUntertitel(rs.getString("utitel"));
                rc.setQuelle(rs.getString("quelle"));
                rc.setIssn(rs.getString("issn"));
                rc.setJahrheft(rs.getString("jahrheft"));
                rc.setVolume(rs.getString("volume"));
                rc.setIssue(rs.getString("issue"));
                rc.setSeiten(rs.getString("seiten"));
                rc.setSprache(rs.getString("sprache"));
                rc.setBeschreibung(rs.getString("beschreibung"));
                rc.setAutorGanz(rs.getString("h_autor"));
                rc.setSchlagworte(getHeadings(rc.getId(), cn));
                list.add(rc);
            }
        } catch (final Exception e) {
            LOG.error("ArrayList<RudolfinerRecord> getAllRecords(Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
        return list;
    }

    public void update(final Long id, final String volume, final String issue, final Connection cn) {
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("UPDATE `artikel` SET " + "`volume` = ?, `issue` = ? WHERE `p_artikel` =?");
            pstmt.setString(1, volume);
            pstmt.setString(2, issue);
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        } catch (final Exception e) {
            LOG.error("update(Long id, String volume, String issue, Connection cn): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("update(Long id, String volume, String issue, Connection cn): " + e.toString());
                }
            }
        }
    }

    public void markAsDublette(final Long id, final Connection cn) {
        PreparedStatement pstmt = null;
        try {
            pstmt = cn.prepareStatement("UPDATE `artikel` SET " + "`dublette` = 1 WHERE `p_artikel` =?");
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (final Exception e) {
            LOG.error("markAsDublette(Long id, Connection cn): " + e.toString());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error("markAsDublette(Long id, Connection cn): " + e.toString());
                }
            }
        }
    }

    private List<String> getHeadings(final Long id, final Connection cn) {
        final List<String> list = new ArrayList<String>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = cn.prepareStatement("SELECT * FROM `schlagwort` AS s INNER JOIN " + "(`artikel_schlagwort` AS a_s) ON (s.p_schlagwort=a_s.f_schlagwort) " + "WHERE a_s.f_artikel = ? order by wort");
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String heading = "";
                heading = rs.getString("wort");
                list.add(heading);
            }
        } catch (final Exception e) {
            LOG.error("ArrayList<String> getHeadings(Long id, Connection cn): " + e.toString());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (final SQLException e) {
                    LOG.error(e.toString());
                }
            }
        }
        return list;
    }

    private List<String> extractNebeneintragungen(String autoren) {
        final List<String> list = new ArrayList<String>();
        while (autoren.contains(", ")) {
            String autor = autoren.substring(0, autoren.indexOf(", ")).trim();
            if (autor.contains(" ")) {
                if (oneSpace(autor)) {
                    autor = returnOneSpace(autor);
                    list.add(autor);
                } else {
                    if (autor.contains(".")) {
                        autor = severalSpacesAndDot(autor);
                        list.add(autor);
                    } else {
                        autor = individualCheck(autor);
                        list.add(autor);
                    }
                }
            } else {
                if (autor.contains(".")) {
                    autor = returnNoSpaceDot(autor);
                    list.add(autor);
                } else {
                    list.add(autor);
                }
            }
            autoren = autoren.substring(autoren.indexOf(", ") + 1);
        }
        return list;
    }

    private boolean oneSpace(String test) {
        boolean check = true;
        test = test.replaceFirst(" ", "");
        if (test.contains(" ")) {
            check = false;
        }
        return check;
    }

    private String returnOneSpace(final String input) {
        String output = input;
        final String praefix = input.substring(0, input.indexOf(' ') + 1).trim();
        output = input.substring(input.indexOf(' ', input.indexOf("") + 1)).trim();
        output = output + ", " + praefix;
        return output;
    }

    private String returnNoSpaceDot(final String input) {
        String output = input;
        final String praefix = input.substring(0, input.lastIndexOf('.') + 1).trim();
        output = input.substring(input.lastIndexOf('.') + 1).trim();
        output = output + ", " + praefix;
        return output;
    }

    private String severalSpacesAndDot(String input) {
        String output = input;
        if (input.contains(" van ") || input.contains(" Van ")) {
            input = input.replaceAll(" Van ", " van ");
            if (input.contains("van de")) {
                final String praefix = input.substring(0, input.indexOf(" ", input.indexOf("van de") + 5)).trim();
                output = input.substring(input.indexOf(" ", input.indexOf("van de") + 5)).trim();
                output = output + ", " + praefix;
            } else {
                final String praefix = input.substring(0, input.indexOf(" ", input.indexOf(" van ") + 2)).trim();
                output = input.substring(input.indexOf(" ", input.indexOf(" van ") + 2)).trim();
                output = output + ", " + praefix;
            }
        } else if (input.contains(" de ")) {
            final String praefix = input.substring(0, input.indexOf(" ", input.indexOf(" de ") + 1)).trim();
            output = input.substring(input.indexOf(" ", input.indexOf(" de ") + 1)).trim();
            output = output + ", " + praefix;
        } else if (input.contains(" De ")) {
            final String praefix = input.substring(0, input.indexOf(" ", input.indexOf(" De ") + 1)).trim();
            output = input.substring(input.indexOf(" ", input.indexOf(" De ") + 1)).trim();
            output = output + ", " + praefix;
        } else {
            final String praefix = input.substring(0, input.lastIndexOf(".") + 1).trim();
            output = input.substring(input.lastIndexOf(".") + 1).trim();
            output = output + ", " + praefix;
        }
        return output;
    }

    private String individualCheck(final String autor) {
        String output = autor;
        if (autor.equals("Huda Huijer Abu-Saad")) {
            output = "Abu-Saad, Huda Huyer";
            return output;
        }
        if (autor.equals("Gian Domenico Borasio")) {
            output = "Borasio, Gian Domenico";
            return output;
        }
        if (autor.equals("Hugh Mc Kenna")) {
            output = "McKenna, Hugh";
            return output;
        }
        if (autor.equals("Bernadette Dierckx de Casterle")) {
            output = "Dierckx de Casterle, Bernadette";
            return output;
        }
        if (autor.equals("Arie Van der Arend")) {
            output = "Arend, Arie van der";
            return output;
        }
        if (autor.equals("Eli Haugen Bunch")) {
            output = "Haugen Bunch, Eli";
            return output;
        }
        if (autor.equals("Ingalill Rahm Hallberg")) {
            output = "Rahm Hallberg, Ingalill";
            return output;
        }
        if (autor.equals("Anne Marie Coll")) {
            output = "Coll, Anne Marie";
            return output;
        }
        if (autor.equals("Oda von Rahden")) {
            output = "Rahden, Oda von";
            return output;
        }
        if (autor.equals("Rena van der Wal")) {
            output = "Wal, Rena van der";
            return output;
        }
        if (autor.equals("Ype Van der Brug")) {
            output = "Brug, Ype Van der";
            return output;
        }
        if (autor.equals("Arno van Raak")) {
            output = "Raak, Arno van";
            return output;
        }
        if (autor.equals("Mark van der Gaag")) {
            output = "Gaag, Mark van der";
            return output;
        }
        if (autor.equals("Theo van Achterberg")) {
            output = "Achterberg, Theo van";
            return output;
        }
        if (autor.equals("Sandra van Dulmen")) {
            output = "Dulmen, Sandra van";
            return output;
        }
        if (autor.equals("Lisa Kane Low")) {
            output = "Kane Low, Lisa";
            return output;
        }
        if (autor.equals("Valeria Lerch Lunardi")) {
            output = "Lerch Lunardi, Valeria";
            return output;
        }
        if (autor.equals("Madeleine Abrandt Dahlgren")) {
            output = "Abrandt Dahlgren, Madeleine";
            return output;
        }
        if (autor.equals("Silvester von Bülow")) {
            output = "Bülow, Silvester von";
            return output;
        }
        if (autor.equals("Iréne von Post")) {
            output = "Post, Iréne von";
            return output;
        }
        if (autor.equals("Isabel Thembi Zwane")) {
            output = "Thembi Zwane, Isabel";
            return output;
        }
        if (autor.equals("Beth Maina Ahlberg")) {
            output = "Maina Ahlberg, Beth";
            return output;
        }
        if (autor.equals("Huda Huijer Abu-Saas")) {
            output = "Abu-Saad, Huda Huyer";
            return output;
        }
        if (autor.equals("Gerrit van der Wal")) {
            output = "Wal, Gerrit van der";
            return output;
        }
        if (autor.equals("Athena Harris Ingall")) {
            output = "Harris Ingall, Athena";
            return output;
        }
        if (autor.equals("Tone Elin Mekki")) {
            output = "Mekki, Tone Elin";
            return output;
        }
        if (autor.equals("Parick Van Gele")) {
            output = "Gele, Parick Van";
            return output;
        }
        if (autor.equals("Irena Anna Frei")) {
            output = "Frei, Irena Anna";
            return output;
        }
        if (autor.equals("Karl Heinz Kristel")) {
            output = "Kristel, Karl Heinz";
            return output;
        }
        if (autor.equals("Gene Cranston Anderson")) {
            output = "Cranston Anderson, Gene";
            return output;
        }
        if (autor.equals("Erik van Rossum")) {
            output = "Rossum, Erik van";
            return output;
        }
        if (autor.equals("Benjamin Ing-Tiau Kuo")) {
            output = "Ing-Tiau Kuon, Benjamin";
            return output;
        }
        if (autor.equals("Marilyn Sawyer Sommers")) {
            output = "Sawyer Sommers, Marilyn";
            return output;
        }
        if (autor.equals("Heinz Günter Niehus")) {
            output = "Niehus, Heinz Günter";
            return output;
        }
        if (autor.equals("Jacomine de Lange")) {
            output = "Lange, Jacomine de";
            return output;
        }
        if (autor.equals("Rianne de Wit")) {
            output = "Wit, Rianne de";
            return output;
        }
        if (autor.equals("Gail Tomblin Murphy")) {
            output = "Tomblin Murphy, Gail";
            return output;
        }
        if (autor.equals("Inmaculada Úbeda Bonet")) {
            output = "Úbeda Bonet, Inmaculada";
            return output;
        }
        if (autor.equals("Lourdes García Vinets")) {
            output = "Vinets, Lourdes García";
            return output;
        }
        if (autor.equals("Bella Bar Cohen")) {
            output = "Bar Cohen, Bella";
            return output;
        }
        if (autor.equals("Ingeborg van den Heuvel")) {
            output = "Heuvel, Ingeborg van den";
            return output;
        }
        if (autor.equals("Jan de Jounge")) {
            output = "Jounge, Jan de";
            return output;
        }
        if (autor.equals("Anne Marie Rafferty")) {
            output = "Rafferty, Anne Marie";
            return output;
        }
        if (autor.equals("Ingo Markus Hinz")) {
            output = "Hinz, Ingo Markus";
            return output;
        }
        if (autor.equals("Sandra Rae Powell")) {
            output = "Rae Powell, Sandra";
            return output;
        }
        if (autor.equals("Janet A Deatrick")) {
            output = "Deatrick, Janet A.";
            return output;
        }
        if (autor.equals("Crétien van Campen")) {
            output = "Campen, Crétien van";
            return output;
        }
        if (autor.equals("Rebecca Baker Russell")) {
            output = "Baker Russell, Rebecca";
            return output;
        }
        if (autor.equals("Jouke van der Zee")) {
            output = "Zee, Jouke van der";
            return output;
        }
        if (autor.equals("Randi Annikki Mortensen")) {
            output = "Annikki Mortensen, Randi";
            return output;
        }
        if (autor.equals("Tor Inge Romeren")) {
            output = "Romeren, Tor Inge";
            return output;
        }
        if (autor.equals("Margaret Shandor Miles")) {
            output = "Shandor Miles, Margaret";
            return output;
        }
        if (autor.equals("Victoria von Sadovszky")) {
            output = "Sadovszky, Victoria von";
            return output;
        }
        if (autor.equals("Crétien van Campen")) {
            output = "Campen, Crétien van";
            return output;
        }
        if (autor.equals("Anita Simoens-De Smet")) {
            output = "Simoens-De Smet, Anita";
            return output;
        }
        if (autor.equals("Myfanwy Lloyd Jones")) {
            output = "Lloyd Jones, Myfanwy";
            return output;
        }
        if (autor.equals("María teresa Marín López")) {
            output = "Marín López, María Teresa";
            return output;
        }
        if (autor.equals("Blanca Mas Hess")) {
            output = "Mas Hess, Blanca";
            return output;
        }
        if (autor.equals("Suzanne Bakken Henry")) {
            output = "Bakken Henry, Suzanne";
            return output;
        }
        if (autor.equals("Mary Ann Johnson")) {
            output = "Johnson, Mary Ann";
            return output;
        }
        if (autor.equals("Afaf Ibrahim Meleis")) {
            output = "Meleis, Afaf Ibrahim";
            return output;
        }
        if (autor.equals("Gwen van Servellen")) {
            output = "Servellen, Gwen van";
            return output;
        }
        if (autor.equals("jan de Jonge")) {
            output = "Jonge, Jan de";
            return output;
        }
        if (autor.equals("Suzanne Bakken Henry")) {
            output = "Bakken Henry, Suzanne";
            return output;
        }
        if (autor.equals("Mary Ann Johnson")) {
            output = "Johnson, Mary Ann";
            return output;
        }
        if (autor.equals("Joyce Newman Giger")) {
            output = "Newman Giger, Joyce";
            return output;
        }
        if (autor.equals("Christine von Känel")) {
            output = "Känel, Christine von";
            return output;
        }
        if (autor.equals("Joan Gyax Spicer")) {
            output = "Gyax Spicer, Joan";
            return output;
        }
        if (autor.equals("Holly Skodol Wilson")) {
            output = "Skodol Wilson, Holly";
            return output;
        }
        if (autor.equals("Barbara Molina Kooker")) {
            output = "Molina Kooker, Barbara";
            return output;
        }
        if (autor.equals("Huda Huyer Abu-Saad")) {
            output = "Abu-Saad, Huda Huyer";
            return output;
        }
        if (autor.equals("Viveca Widmark Peterson")) {
            output = "Dulmen";
            return output;
        }
        if (autor.equals("Widmark Peterson, Viveca")) {
            output = "Dulmen";
            return output;
        }
        if (autor.equals("Louise von Essen")) {
            output = "Essen, Louise von";
            return output;
        }
        if (autor.equals("Karen Hassey Dow")) {
            output = "Hassey Dow, Karen";
            return output;
        }
        if (autor.equals("Laila Bana Ahmed")) {
            output = "Bana Ahmed, Laila";
            return output;
        }
        if (autor.equals("Judith Belle Brown")) {
            output = "Belle Brown, Judith";
            return output;
        }
        if (autor.equals("Jan Willem Louwerens")) {
            output = "Louwerens, Jan Willem";
            return output;
        }
        if (autor.equals("Esperanza Torres de Ardon")) {
            output = "Torres de Ardon, Esperanza";
            return output;
        }
        if (autor.equals("Alia Obeid Al-Kaabi")) {
            output = "Al-Kaabi, Alia Obeid";
            return output;
        }
        if (autor.equals("Renate Schwarz Govaers")) {
            output = "Schwarz Govaers, Renate";
            return output;
        }
        if (autor.equals("Dorothy Hendel Clough")) {
            output = "Hendel Clough, Dorothy";
            return output;
        }
        if (autor.equals("Inger Margrethe Holter")) {
            output = "Holter, Inger Margrethe";
            return output;
        }
        if (autor.equals("Sherry Garrett Hendrickson")) {
            output = "Garrett Hendrickson, Sherry";
            return output;
        }
        if (autor.equals("Penny King Ericson")) {
            output = "King Ericson, Penny";
            return output;
        }
        if (autor.equals("Ulla Welander Hansson")) {
            output = "Welander Hansson, Ulla";
            return output;
        }
        if (autor.equals("Lesley Anne Ezelle")) {
            output = "Ezelle, Lesley Anne";
            return output;
        }
        if (autor.equals("Kathleen Von Dollen")) {
            output = "Dollen, Kathleen von";
            return output;
        }
        if (autor.equals("Anne Marie Arseneault")) {
            output = "Arseneault, Anne Marie";
            return output;
        }
        if (autor.equals("Peggy Anne Field")) {
            output = "Field, Peggy Anne";
            return output;
        }
        if (autor.equals("Patrice Kinneally Nicholas")) {
            output = "Kinneally Nicholas, Patrice";
            return output;
        }
        if (autor.equals("Mary Ann Christ")) {
            output = "Christ, Mary Ann";
            return output;
        }
        if (autor.equals("Kathleen McGlynn Shadick")) {
            output = "McGlynn Shadick, Kathleen";
            return output;
        }
        if (autor.equals("Janet Ross Kerr")) {
            output = "Ross Kerr, Janet";
            return output;
        }
        if (autor.equals("Marjolein van Vliet")) {
            output = "Vliet, Marjolein van";
            return output;
        }
        if (autor.equals("Winsome St John")) {
            output = "St John, Winsome";
            return output;
        }
        if (autor.equals("Murray John Fisher")) {
            output = "John Fisher, Murray";
            return output;
        }
        if (autor.equals("Marit Van de Ven")) {
            output = "Ven, Marit Van de";
            return output;
        }
        if (autor.equals("Bernadette Dierckx de Casterlé")) {
            output = "Dierckx de Casterlé, Bernadette";
            return output;
        }
        if (autor.equals("Paulette Van Oost")) {
            output = "Oost, Paulette van";
            return output;
        }
        if (autor.equals("Anne Marie Depoorter")) {
            output = "Depoorter, Anne Marie";
            return output;
        }
        if (autor.equals("Zohreh Parsa Yekta")) {
            output = "Parsa Yekta, Zohreh";
            return output;
        }
        if (autor.equals("Jose María Roa")) {
            output = "Roa, Jose María";
            return output;
        }
        if (autor.equals("Francisco Pedro Garcia-Fernadez")) {
            output = "Garcia-Fernadez, Francisco Pedro";
            return output;
        }
        if (autor.equals("Volker Eric Amelung")) {
            output = "Amelung, Volker Eric";
            return output;
        }
        if (autor.equals("Edwin van Teijlingen")) {
            output = "Teijlingen, Edwin van";
            return output;
        }
        if (autor.equals("Anna Riita Jyrkinen")) {
            output = "Jyrkinen, Anna Riita";
            return output;
        }
        if (autor.equals("Mary Lee Lacy")) {
            output = "Lacy, Mary Lee";
            return output;
        }
        if (autor.equals("Pádraig Mac Neela")) {
            output = "Mac Neela, Pádraig";
            return output;
        }
        if (autor.equals("Maria Sylvia Campos")) {
            output = "Campos, Maria Sylvia";
            return output;
        }
        if (autor.equals("Mary Jane Jacobsen")) {
            output = "Jacobsen, Mary Jane";
            return output;
        }
        if (autor.equals("Georges Van Maele")) {
            output = "Maele, Georges Van";
            return output;
        }
        if (autor.equals("Marjon van der Pol")) {
            output = "Pol, Marjon van der";
            return output;
        }
        if (autor.equals("Ann Van Hecke")) {
            output = "Hecke, Ann van";
            return output;
        }
        if (autor.equals("Mary Ann Lavin")) {
            output = "Lavin, Mary Ann";
            return output;
        }
        if (autor.equals("Paul Maurice Conway")) {
            output = "Conway, Paul Maurice";
            return output;
        }
        if (autor.equals("Beatrice Isabella Johanna Maria Van der Heijden")) {
            output = "Heijden, Beatrice Isabella Johanna Maria Van der";
            return output;
        }
        if (autor.equals("Linda McGillis Hall")) {
            output = "McGillis Hall, Linda";
            return output;
        }
        if (autor.equals("Linda O'Brien Pallas")) {
            output = "O'Brien Pallas, Linda";
            return output;
        }
        if (autor.equals("Abdul Majeed Alhashem")) {
            output = "Majeed Alhashem, Abdul";
            return output;
        }
        if (autor.equals("Robert Vander Stichel")) {
            output = "Stichel, Robert van der";
            return output;
        }
        if (autor.equals("Nele Van Den Noortgate")) {
            output = "Noortgate, Nele dan den";
            return output;
        }
        if (autor.equals("Andrea Dobrin Schippers")) {
            output = "Dobrin Schippers, Andrea";
            return output;
        }
        if (autor.equals("Vicki Jo Carrier")) {
            output = "Carrier, Vicki Jo";
            return output;
        }
        if (autor.equals("Diana Tze Fan Lee")) {
            output = "Tze Fan Lee, Diana";
            return output;
        }
        if (autor.equals("Sandra von Komorowsky")) {
            output = "Komorowsky, Sandra von";
            return output;
        }
        if (autor.equals("Ali Osman Ozturk")) {
            output = "Osman Ozturk, Ali";
            return output;
        }
        if (autor.equals("Frances Kam Yuet Wong")) {
            output = "Kam Yuet Wong, Frances";
            return output;
        }
        if (autor.equals("Francisco Pedro García-Fernández")) {
            output = "García-Fernández, Francisco Pedro";
            return output;
        }
        if (autor.equals("Isabel Maria López-Medina")) {
            output = "López-Medina, Isabel Maria";
            return output;
        }
        if (autor.equals("Julie Duff Cloutier")) {
            output = "Duff Cloutier, Julie";
            return output;
        }
        if (autor.equals("Robyn Gail Kell")) {
            output = "Gail Kell, Robyn";
            return output;
        }
        if (autor.equals("Ülkü Yapucu Günes")) {
            output = "Yapucu Günes, Ülkü";
            return output;
        }
        if (autor.equals("Joan Stehle Werner")) {
            output = "Stehle Werner, Joan";
            return output;
        }
        if (autor.equals("Mary Kay Rayens")) {
            output = "Kay Rayens, Mary";
            return output;
        }
        if (autor.equals("Clare E Collins")) {
            output = "Collins, Clare E.";
            return output;
        }
        if (autor.equals("Bernd von Hallern")) {
            output = "Hallern, Bernd von";
            return output;
        }
        if (autor.equals("Karen Bulmer Smith")) {
            output = "Bulmer Smith, Karen";
            return output;
        }
        if (autor.equals("May Ann Van Eps")) {
            output = "Eps, May Ann van";
            return output;
        }
        if (autor.equals("Liesbeth De Blaeser")) {
            output = "Blaeser, Liesbeth De";
            return output;
        }
        if (autor.equals("Therese Stutz Steiger")) {
            output = "Stutz Steiger, Therese";
            return output;
        }
        if (autor.equals("Siti Zubaidah Mordiffi")) {
            output = "Zubaidah Mordiffi, Siti";
            return output;
        }
        if (autor.equals("Bodo de Vries")) {
            output = "Vries, Bodo de";
            return output;
        }
        if (autor.equals("Mara Ann Lavin")) {
            output = "Lavin, Mary Ann";
            return output;
        }
        if (autor.equals("Jacqueline de Leeuw")) {
            output = "Leeuw, Jacqueline de";
            return output;
        }
        if (autor.equals("Ingrid Pramling Samuelsson")) {
            output = "Pramling Samuelsson, Ingrid";
            return output;
        }
        if (autor.equals("Annika Schulze Geiping")) {
            output = "Schulze Geiping, Annika";
            return output;
        }
        if (autor.equals("Mary Ann Murray")) {
            output = "Murray, Mary Ann";
            return output;
        }
        if (autor.equals("Donna Zimmaro Bliss")) {
            output = "Zimmaro Bliss, Donna";
            return output;
        }
        if (autor.equals("Cindy Du Bois")) {
            output = "Du Bois, Cindy";
            return output;
        }
        if (autor.equals("Sara De Gieter")) {
            output = "Gieter, Sara De";
            return output;
        }
        if (autor.equals("Rein de Cooman")) {
            output = "Cooman, Rein de";
            return output;
        }
        if (autor.equals("Berno von Meijel")) {
            output = "Meijel, Berno von";
            return output;
        }
        if (autor.equals("Alice Yuen Loke")) {
            output = "Yuen Loke, Alice";
            return output;
        }
        if (autor.equals("William George Kernohan")) {
            output = "Kernohan, William George";
            return output;
        }
        if (autor.equals("Roman Frank Oppermann")) {
            output = "Oppermann, Roman Frank";
            return output;
        }
        if (autor.equals("Neeltje van den Berg")) {
            output = "Berg, Neeltje van den";
            return output;
        }
        if (autor.equals("Peter De Jonge")) {
            output = "Jonge, Peter De";
            return output;
        }
        if (autor.equals("Marie Ann Lavin")) {
            output = "Lavin, Mary Ann";
            return output;
        }
        if (autor.equals("Hallvard Andreas Vindenes")) {
            output = "Vindenes, Hallvard Andreas";
            return output;
        }
        if (autor.equals("Paola Di Giulio")) {
            output = "Di Giulio, Paola";
            return output;
        }
        if (autor.equals("Horst Christian Vollmar")) {
            output = "Vollmar, Horst Christian";
            return output;
        }
        if (autor.equals("May Solveig Fagermoen")) {
            output = "Solveig Fagermoen, May";
            return output;
        }
        if (autor.equals("Jeffrey Damon Dagnone")) {
            output = "Dagnone, Jeffrey Damon";
            return output;
        }
        if (autor.equals("Irena Anna Frei")) {
            output = "Frei, Irena Anna";
            return output;
        }
        if (autor.equals("Hans Ruedi Stoll")) {
            output = "Stoll, Hans Ruedi";
            return output;
        }
        if (autor.equals("Gordon Allen Finley")) {
            output = "Finley, Gordon Allen";
            return output;
        }
        if (autor.equals("Wenche Bergseth Bogsti")) {
            output = "Bergseth Bogsti, Wenche";
            return output;
        }
        if (autor.equals("Elisabeth Gulowsen Celius")) {
            output = "Gulowsen Celius, Elisabeth";
            return output;
        }
        if (autor.equals("Mary Anne Simpson")) {
            output = "Simpson, Mary Anne";
            return output;
        }
        if (autor.equals("Daniel Yee Tak Fong")) {
            output = "Yee Tak Fong, Daniel";
            return output;
        }
        if (autor.equals("Meghan McGonigal Kenney")) {
            output = "McGonigal Kenney, Meghan";
            return output;
        }
        if (autor.equals("Erla Kolbrun Svavarsdottir")) {
            output = "Kolbrun Svavarsdottir, Erla";
            return output;
        }
        if (autor.equals("Mary Kay Rayens")) {
            output = "Kay Rayens, Mary";
            return output;
        }
        if (autor.equals("Helena Syna Desivilya")) {
            output = "Syna Desivilya, Helena";
            return output;
        }
        if (autor.equals("Piet van Riel")) {
            output = "Riel, Piet van";
            return output;
        }
        if (autor.equals("Cristina Maria Galvao")) {
            output = "Galvao, Cristina Maria";
            return output;
        }
        if (autor.equals("Monika von Detten")) {
            output = "Detten, Monika von";
            return output;
        }
        if (autor.equals("Carolien de Blok")) {
            output = "Blok, Carolien de";
            return output;
        }
        if (autor.equals("Jörg große Schlarmann")) {
            output = "Große Schlarmann, Jörg";
            return output;
        }
        if (autor.equals("Pia von Lützau")) {
            output = "Lützau, Pia von";
            return output;
        }
        if (autor.equals("Mehmet Emin Orhan")) {
            output = "Emin Orhan, Mehmet";
            return output;
        }
        if (autor.equals("Lais Helena Ramos")) {
            output = "Ramos, Lais Helena";
            return output;
        }
        if (autor.equals("Antonia Pollidoro Bonghi")) {
            output = "Pollidoro Bonghi, Antonia";
            return output;
        }
        if (autor.equals("Luisa Di Labio")) {
            output = "Di Labio, Luisa";
            return output;
        }
        if (autor.equals("Ursi Barandun Schäfer")) {
            output = "Barandun Schäfer, Ursi";
            return output;
        }
        if (autor.equals("Maria Magdalena Schreier")) {
            output = "Schreier, Maria Magdalena";
            return output;
        }
        return output;
    }

    private static String extractSpage(final String seiten) {
        String spage = "";
        if (seiten != null && seiten.length() != 0) {
            try {
                final Pattern z = Pattern.compile("[0-9]*");
                final Matcher w = z.matcher(seiten);
                if (w.find()) {
                    spage = seiten.substring(w.start(), w.end());
                }
                if (spage.equals("")) {
                    spage = seiten;
                }
            } catch (final Exception e) {
                System.out.println("extractSpage: " + seiten + "\040" + e.toString());
            }
        }
        return spage;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getJahrheft() {
        return jahrheft;
    }

    public void setJahrheft(final String jahrheft) {
        this.jahrheft = jahrheft;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(final String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(final String issue) {
        this.issue = issue;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(final String sprache) {
        this.sprache = sprache;
    }

    public String getJahr() {
        return jahr;
    }

    public void setJahr(final String jahr) {
        this.jahr = jahr;
    }

    public String getAutorVorname() {
        return autorVorname;
    }

    public void setAutorVorname(final String autorVorname) {
        this.autorVorname = autorVorname;
    }

    public String getAutorNachname() {
        return autorNachname;
    }

    public void setAutorNachname(final String autorNachname) {
        this.autorNachname = autorNachname;
    }

    public String getAutorGanz() {
        return autorGanz;
    }

    public void setAutorGanz(final String autorGanz) {
        this.autorGanz = autorGanz;
    }

    public String getAutoren() {
        return autoren;
    }

    public void setAutoren(final String autoren) {
        this.autoren = autoren;
    }

    public String getHaupttitel() {
        return haupttitel;
    }

    public void setHaupttitel(final String haupttitel) {
        this.haupttitel = haupttitel;
    }

    public String getUntertitel() {
        return untertitel;
    }

    public void setUntertitel(final String untertitel) {
        this.untertitel = untertitel;
    }

    public String getQuelle() {
        return quelle;
    }

    public void setQuelle(final String quelle) {
        this.quelle = quelle;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(final String issn) {
        this.issn = issn;
    }

    public String getSeiten() {
        return seiten;
    }

    public void setSeiten(final String seiten) {
        this.seiten = seiten;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(final String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public List<String> getSchlagworte() {
        return schlagworte;
    }

    public void setSchlagworte(final List<String> schlagworte) {
        this.schlagworte = schlagworte;
    }

    public List<String> getNebeneintragungen() {
        return nebeneintragungen;
    }

    public void setNebeneintragungen(final List<String> nebeneintragungen) {
        this.nebeneintragungen = nebeneintragungen;
    }
}
