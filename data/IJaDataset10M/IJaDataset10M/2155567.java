package org.bibnet.weg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bibnet.entity.Article;
import org.bibnet.entity.Marcfield;
import org.bibnet.entity.Subfield;
import org.bibnet.weg.helper.ISSN;
import org.grlea.log.SimpleLogger;

public class CompleteWEG {

    private static final SimpleLogger LOG = new SimpleLogger(CompleteWEG.class);

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        String zeile = "";
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            in = new BufferedReader(new FileReader("/home/flyingfischer/Desktop/Import/WEG/WEGArtikel.xml"));
            out = new BufferedWriter(new FileWriter("/home/flyingfischer/Desktop/Import/WEG/WEGArtikel-corr-bearb.xml"));
            int currentTagNumber = 0;
            boolean write773 = true;
            boolean write260 = true;
            Long systemId = Long.valueOf("1000");
            Article art = new Article();
            final StringBuffer buf = new StringBuffer();
            while ((zeile = in.readLine()) != null) {
                if (zeile.contains("</record>")) {
                    currentTagNumber = 0;
                    write773 = true;
                    write260 = true;
                    write260(art, buf);
                    write773(art, buf);
                }
                if (currentTagNumber == 0) {
                    art = new Article();
                }
                if (zeile.contains("<datafield tag=\"")) {
                    currentTagNumber = Integer.valueOf(zeile.substring(zeile.indexOf("<datafield tag=\"") + 16, zeile.indexOf("<datafield tag=\"") + 19));
                }
                if (zeile.contains("<datafield tag=\"245\"")) {
                    buf.append(zeile);
                    buf.append('\n');
                    while ((zeile = in.readLine()) != null) {
                        if (zeile.contains("</datafield>")) {
                            break;
                        } else if (zeile.contains("[in:")) {
                            final String tmp = extractSubfieldContent(zeile);
                            final String f733g = tmp.substring(tmp.indexOf("[in:"));
                            final int posYear = positionYear(f733g);
                            art.setField773s(extractJournal(f733g, posYear));
                            art.setField773g(extract773g(f733g, posYear, art));
                            if (art.getField773g() != null) {
                                parse773g(art);
                            }
                            final String is = ISSN.complete(art.getField773s());
                            if (!"".equals(is)) {
                                art.setField773x(is);
                            }
                            zeile = remove773FromTitle(zeile);
                            if (posYear != 0) {
                                art.setField260c(f733g.substring(posYear, posYear + 4));
                            }
                            if ("Entwicklungspsychologie / Silbereisen/Montada".equals(art.getField773s())) {
                                art.setField773z("3621104615");
                            }
                        }
                        buf.append(zeile);
                        buf.append('\n');
                    }
                }
                if (zeile.contains("<datafield tag=\"260\"")) {
                    final Marcfield mf = new Marcfield();
                    mf.setNumber("260");
                    final List<Subfield> list = new ArrayList<Subfield>();
                    while ((zeile = in.readLine()) != null) {
                        if (zeile.contains("</datafield>")) {
                            break;
                        } else if (zeile.contains("code=\"a\"")) {
                            list.add(new Subfield('a', extractSubfieldContent(zeile)));
                        } else if (zeile.contains("code=\"b\"")) {
                            list.add(new Subfield('b', extractSubfieldContent(zeile)));
                        } else if (zeile.contains("code=\"c\"")) {
                            list.add(new Subfield('c', extractSubfieldContent(zeile)));
                        }
                    }
                    mf.setSubfields(list);
                    art.setField260(mf);
                    zeile = in.readLine();
                }
                if (write260 && currentTagNumber >= 260) {
                    write260 = false;
                    write260(art, buf);
                }
                if (write773 && currentTagNumber > 773) {
                    write773 = false;
                    write773(art, buf);
                }
                buf.append(zeile);
                buf.append('\n');
                if (zeile.contains("<leader>")) {
                    buf.append("\t\t<controlfield tag=\"001\">WEG");
                    buf.append(systemId);
                    buf.append("</controlfield>\n");
                    systemId++;
                }
            }
            out.write(buf.toString());
        } catch (final IOException e) {
            System.err.println("Error reading file!");
        } finally {
            try {
                in.close();
                out.close();
                System.out.println("fertig!");
            } catch (final Exception e) {
                LOG.error(e.toString());
            }
        }
    }

    private static String extractJournal(final String input, final int posYear) {
        String journal = "";
        try {
            if (posYear > 0) {
                journal = input.substring(0, posYear);
                journal = journal.substring(journal.indexOf("[in:") + 4, journal.lastIndexOf(',')).trim();
            } else {
                journal = input;
                journal = journal.substring(journal.indexOf("[in:") + 4).trim();
            }
        } catch (final Exception e) {
            System.out.println("extractJournal(final String input, final int posYear): " + input + "\040" + e.toString());
        }
        if (journal.contains("[") || journal.contains("]")) {
            journal = correctBracket(journal);
        }
        return journal;
    }

    private static String extract773g(final String input, final int posYear, final Article art) {
        String result = "";
        result = input.substring(posYear);
        if (result.endsWith("] /")) {
            result = result.substring(0, result.length() - 3);
        }
        if (result.endsWith("]") && !result.contains("[")) {
            result = result.substring(0, result.length() - 1);
        }
        if (result.endsWith("]]")) {
            result = result.replaceAll("\\]\\]", "]");
        }
        if (result.startsWith("[in: ")) {
            result = result.substring(5);
        }
        final String copy = result;
        if (result.contains("; Fortsetzung")) {
            result = result.substring(0, result.indexOf("; Fortsetzung")).trim();
            art.setField500(new Marcfield("500", 'a', copy.substring(copy.indexOf("Fortsetzung"))));
        }
        if (result.contains(". - Erratum in:")) {
            result = result.substring(0, result.indexOf(". - Erratum in:"));
            art.setField500(new Marcfield("500", 'a', copy.substring(copy.indexOf("Erratum in:"))));
        }
        return result;
    }

    private static void parse773g(final Article art) {
        final String f773g = art.getField773g();
        if (f773g.contains("vol.")) {
            art.setField773v(extractReferenceElement(f773g, "vol."));
        }
        if (f773g.contains("no.")) {
            art.setField773l(extractReferenceElement(f773g, "no."));
        }
        if (f773g.contains("p.")) {
            art.setField773j(extractReferenceElement(f773g, "p."));
        }
        if (art.getField773j() != null) {
            art.setField773q(parseSpage(art.getField773j()));
        }
    }

    private static String extractReferenceElement(final String input, final String identifier) {
        String result = input.substring(input.indexOf(identifier) + identifier.length()).trim();
        if (result.contains(",")) {
            result = result.substring(0, result.indexOf(',')).trim();
        } else if (result.contains(" ")) {
            result = result.substring(0, result.indexOf(' ')).trim();
        }
        return result;
    }

    private static String parseSpage(final String input) {
        String result = "";
        final Pattern pat = Pattern.compile("[0-9]+");
        final Matcher match = pat.matcher(input);
        try {
            if (match.find()) {
                result = input.substring(match.start(), match.end());
            }
        } catch (final Exception e) {
            System.out.println("positionYear(String input): " + input + "\040" + e.toString());
        }
        return result;
    }

    private static void write260(final Article art, final StringBuffer buf) {
        if (art.getField260() != null || art.getField260c() != null) {
            buf.append("\t\t<datafield tag=\"260\" ind1=\" \" ind2=\" \">\n");
            boolean c = false;
            if (art.getField260() != null) {
                for (final Subfield sf : art.getField260().getSubfields()) {
                    if (sf.getDefinition() == 'a') {
                        buf.append("\t\t\t<subfield code=\"a\">");
                        buf.append(sf.getContent());
                        buf.append("</subfield>\n");
                    } else if (sf.getDefinition() == 'b') {
                        buf.append("\t\t\t<subfield code=\"b\">");
                        buf.append(sf.getContent());
                        buf.append("</subfield>\n");
                    } else if (sf.getDefinition() == 'c') {
                        c = true;
                        buf.append("\t\t\t<subfield code=\"c\">");
                        buf.append(sf.getContent());
                        buf.append("</subfield>\n");
                    }
                }
            }
            if (!c && art.getField260c() != null) {
                buf.append("\t\t\t<subfield code=\"c\">");
                buf.append(art.getField260c());
                buf.append("</subfield>");
                buf.append('\n');
            }
            buf.append("\t\t</datafield>");
            buf.append('\n');
        }
        art.setField260(null);
        art.setField260c(null);
    }

    private static void write773(final Article art, final StringBuffer buf) {
        if (art.getField773s() != null || art.getField773g() != null || art.getField773j() != null || art.getField773l() != null || art.getField773q() != null || art.getField773v() != null || art.getField773x() != null || art.getField773x() != null) {
            buf.append("\t\t<datafield tag=\"773\" ind1=\" \" ind2=\" \">");
            buf.append('\n');
            if (art.getField773g() != null) {
                buf.append("\t\t\t<subfield code=\"g\">");
                buf.append(art.getField773g());
                buf.append("</subfield>");
                buf.append('\n');
                art.setField773g(null);
            }
            if (art.getField773j() != null) {
                buf.append("\t\t\t<subfield code=\"j\">");
                buf.append(art.getField773j());
                buf.append("</subfield>");
                buf.append('\n');
                art.setField773j(null);
            }
            if (art.getField773l() != null) {
                buf.append("\t\t\t<subfield code=\"l\">");
                buf.append(art.getField773l());
                buf.append("</subfield>");
                buf.append('\n');
                art.setField773l(null);
            }
            if (art.getField773q() != null) {
                buf.append("\t\t\t<subfield code=\"q\">");
                buf.append(art.getField773q());
                buf.append("</subfield>");
                buf.append('\n');
                art.setField773q(null);
            }
            if (art.getField773s() != null) {
                buf.append("\t\t\t<subfield code=\"s\">");
                buf.append(art.getField773s());
                buf.append("</subfield>");
                buf.append('\n');
                art.setField773s(null);
            }
            if (art.getField773v() != null) {
                buf.append("\t\t\t<subfield code=\"v\">");
                buf.append(art.getField773v());
                buf.append("</subfield>");
                buf.append('\n');
                art.setField773v(null);
            }
            if (art.getField773x() != null) {
                buf.append("\t\t\t<subfield code=\"x\">");
                buf.append(art.getField773x());
                buf.append("</subfield>");
                buf.append('\n');
                art.setField773x(null);
            }
            if (art.getField773z() != null) {
                buf.append("\t\t\t<subfield code=\"z\">");
                buf.append(art.getField773z());
                buf.append("</subfield>");
                buf.append('\n');
                art.setField773z(null);
            }
            buf.append("\t\t</datafield>");
            buf.append('\n');
        }
    }

    private static String remove773FromTitle(final String input) {
        String result = input;
        if (input.endsWith("/</subfield>")) {
            result = input.substring(0, result.indexOf("[in:"));
            if (result.endsWith(" ")) {
                result = result + "/</subfield>";
            } else {
                result = result + " /</subfield>";
            }
        } else {
            result = input.substring(0, result.indexOf("[in:")).trim();
            result = "\t\t\t" + result + "</subfield>";
        }
        return result;
    }

    private static int positionYear(final String input) {
        int pos = 0;
        final Pattern pat = Pattern.compile("[0-9]{4}");
        final Matcher match = pat.matcher(input);
        try {
            if (match.find()) {
                pos = match.start();
            }
        } catch (final Exception e) {
            System.out.println("positionYear(String input): " + input + "\040" + e.toString());
        }
        return pos;
    }

    /**
	 * Extracts the content of a subfield with a given code
	 *
	 */
    private static String extractSubfieldContent(final String line) {
        String output = "";
        try {
            final String identifier = "<subfield code=\"";
            if (line.contains(identifier)) {
                output = line.substring(line.indexOf(identifier) + 19, line.indexOf("</subfield>")).trim();
            }
        } catch (final Exception e) {
            System.out.println(line + " | " + e);
        }
        return output;
    }

    private static String correctBracket(final String input) {
        String journal = input;
        journal = journal.replaceAll("\\[", "");
        journal = journal.replaceAll("\\]", "");
        if (journal.charAt(journal.length() - 1) == '/') {
            journal = journal.substring(0, journal.length() - 2);
        }
        journal = journal.replaceAll("Einzelartikel", "[Einzelartikel]");
        journal = journal.replaceAll("Referat", "[Referat]");
        return journal;
    }
}
