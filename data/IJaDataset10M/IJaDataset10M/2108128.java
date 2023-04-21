package org.bibnet.playground;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.grlea.log.SimpleLogger;
import util.DBConn;

public class RudolfinerReadVolume {

    private static final SimpleLogger LOG = new SimpleLogger(RudolfinerReadVolume.class);

    public static void main(final String[] args) {
        final RudolfinerRecord record = new RudolfinerRecord();
        BufferedWriter out = null;
        final DBConn cn = new DBConn();
        final List<RudolfinerRecord> list = record.getAllRecords(false, cn.getSingleConnection());
        try {
            out = new BufferedWriter(new FileWriter("/home/flyingfischer/Desktop/Import/rudolfiner.txt"));
            for (final RudolfinerRecord rc : list) {
                final String jahrheft = rc.getJahrheft();
                String volume = "";
                String issue = "";
                String rest = jahrheft;
                if (jahrheft.contains("Jg")) {
                    volume = jahrheft.substring(0, jahrheft.indexOf("Jg"));
                    rest = jahrheft.substring(jahrheft.indexOf("Jg") + 2);
                    if (volume.contains(".")) {
                        volume = volume.substring(0, volume.indexOf('.')).trim();
                    }
                    if (volume.contains("Sondernummer, ")) {
                        volume = volume.substring(volume.indexOf("Sondernummer, ") + 13).trim();
                        rest = jahrheft;
                    }
                    if (jahrheft.contains("Jg. 45")) {
                        volume = "45";
                        rest = jahrheft.substring(jahrheft.indexOf("Jg. 45") + 6);
                    }
                } else if (jahrheft.contains("jg")) {
                    volume = jahrheft.substring(0, jahrheft.indexOf("jg"));
                    rest = jahrheft.substring(jahrheft.indexOf("jg") + 2);
                    if (volume.contains(".")) {
                        volume = volume.substring(0, volume.indexOf('.')).trim();
                    }
                    if (volume.contains("Sondernummer, ")) {
                        volume = volume.substring(volume.indexOf("Sondernummer, ") + 13).trim();
                        rest = jahrheft;
                    }
                    if (jahrheft.contains("jg. 45")) {
                        volume = "45";
                        rest = jahrheft.substring(jahrheft.indexOf("jg. 45") + 6);
                    }
                } else if (jahrheft.contains("Vol.")) {
                    volume = jahrheft.substring(jahrheft.indexOf("Vol.") + 4).trim();
                    final String testForPoint = volume.substring(1, 3);
                    if (testForPoint.contains(".")) {
                        volume = volume.substring(0, volume.indexOf('.'));
                        final String tmpVol = "Vol. " + volume;
                        rest = jahrheft.substring(jahrheft.indexOf("tmpVol") + tmpVol.length() + 1);
                    } else {
                        volume = volume.substring(0, volume.indexOf(", "));
                        final String tmpVol = "Vol. " + volume;
                        rest = jahrheft.substring(jahrheft.indexOf("tmpVol") + tmpVol.length() + 1);
                    }
                }
                issue = getIssue(rest);
                System.out.println(rc.getId() + ":" + volume + "|" + issue);
                record.update(rc.getId(), volume, removeLeadingZero(issue), cn.getConnection());
            }
        } catch (final IOException e) {
            LOG.error(e.toString());
        } finally {
            try {
                out.close();
                cn.close();
            } catch (final Exception e) {
                LOG.error(e.toString());
            }
        }
    }

    private static String removeStartInterpunction(String rest) {
        while (rest.charAt(0) == '.' || rest.startsWith(", ")) {
            rest = rest.substring(1).trim();
        }
        return rest;
    }

    private static String removeLeadingZero(String issue) {
        while (issue.charAt(0) == '0') {
            issue = issue.substring(1).trim();
        }
        return issue;
    }

    private static String getIssue(final String rest) {
        String issue = "";
        issue = removeStartInterpunction(rest);
        if (issue.contains("Heft 3 Juli")) {
            issue = "3";
        } else if (issue.contains("Heft 5.")) {
            issue = "5";
        } else if (issue.contains("Heft Nr.")) {
            issue = identify(issue, "Heft Nr.");
        } else if (issue.contains("Heft")) {
            issue = identify(issue, "Heft");
        } else if (issue.contains("Nr.11-12")) {
            issue = "11-12";
        } else if (issue.contains("No.")) {
            issue = identify(issue, "No.");
        } else if (issue.contains("No")) {
            if (issue.contains("November")) {
                if (issue.contains("No ")) {
                    issue = identify(issue, "No ");
                }
            } else {
                issue = identify(issue, "No");
            }
        } else if (issue.contains("Issue")) {
            issue = identify(issue, "Issue");
        } else if (issue.contains("Ausgabe")) {
            issue = identify(issue, "Ausgabe");
        } else {
            final Pattern p = Pattern.compile("([0-9]{1,2})/([0-9]{2,4})");
            final Matcher m = p.matcher(issue);
            if (m.find()) {
                System.out.println(issue);
                issue = issue.substring(m.start(), m.end());
                issue = issue.substring(0, issue.indexOf('/'));
            } else {
                final Pattern p2 = Pattern.compile("([0-9]{1,2})/([0-9]{2,4})");
                final Matcher m2 = p2.matcher(issue);
                if (m2.find()) {
                    System.out.println(issue);
                    issue = issue.substring(m2.start(), m2.end());
                    issue = issue.substring(0, issue.indexOf('/'));
                } else if (issue.contains("Nr.")) {
                    issue = identify(issue, "Nr.");
                } else {
                    final Pattern p1 = Pattern.compile("([0-9]{1,2})\\.([0-9]{2,4})");
                    final Matcher m1 = p1.matcher(issue);
                    if (m1.find()) {
                        System.out.println(issue);
                        issue = issue.substring(m1.start(), m1.end());
                        issue = issue.substring(0, issue.indexOf('.'));
                    }
                }
            }
        }
        if (issue.contains("Nr.11")) {
            issue = "11";
        }
        if (issue.contains("Nr. 4")) {
            issue = "4";
        }
        if (issue.contains("Nr.92")) {
            issue = "92";
        }
        if (issue.contains("H. 1")) {
            issue = "1";
        }
        if (issue.contains("H. 2")) {
            issue = "2";
        }
        if (issue.contains("H. 3")) {
            issue = "3";
        }
        if (issue.contains("H. 4")) {
            issue = "4";
        }
        if (issue.contains("Ausgabe 2")) {
            issue = "2";
        }
        if (issue.contains("No.36-37")) {
            issue = "36-37";
        }
        if (issue.contains("7/8")) {
            issue = "7/8";
        }
        if (issue.contains("Juli-August")) {
            issue = "Juli/August";
        }
        if (issue.contains("Oktober/November")) {
            issue = "Oktober/November";
        }
        if (issue.contains("Juni-giugno")) {
            issue = "Juni-giugno";
        }
        if (issue.contains("julio/agosto")) {
            issue = "julio/agosto";
        }
        if (issue.contains("Januar")) {
            issue = "Januar";
        }
        if (issue.contains("Jänner")) {
            issue = "Januar";
        }
        if (issue.contains("Februar")) {
            issue = "Februar";
        }
        if (issue.contains("März")) {
            issue = "März";
        }
        if (issue.contains("April")) {
            issue = "April";
        }
        if (issue.contains("Mai")) {
            issue = "Mai";
        }
        if (issue.contains("Juni")) {
            issue = "Juni";
        }
        if (issue.contains("Juli")) {
            issue = "Juli";
        }
        if (issue.contains("August")) {
            issue = "August";
        }
        if (issue.contains("September")) {
            issue = "September";
        }
        if (issue.contains("Oktober")) {
            issue = "Oktober";
        }
        if (issue.contains("November")) {
            issue = "November";
        }
        if (issue.contains("Dezember")) {
            issue = "Dezember";
        }
        return issue;
    }

    private static String identify(String issue, final String identifier) {
        String result = issue;
        final int x = issue.substring(issue.indexOf(identifier) + identifier.length()).length();
        int add = 0;
        if (x > 5) {
            add = 6;
        } else {
            add = x;
        }
        String tmpTest = issue.substring(issue.indexOf(identifier) + identifier.length() + 0, issue.indexOf(identifier) + identifier.length() + add);
        if (tmpTest.contains(", ")) {
            result = issue.substring(issue.indexOf(identifier) + identifier.length(), issue.indexOf(", ", issue.indexOf(identifier))).trim();
        } else {
            issue = issue.substring(issue.indexOf("identifier") + identifier.length() + 1).trim();
            if (issue.contains("/")) {
                tmpTest = issue.substring(0, issue.indexOf('/'));
                if (!tmpTest.contains(" ")) {
                    result = tmpTest;
                } else {
                    result = issue.substring(0, issue.indexOf(' '));
                    if (result.endsWith(", ")) {
                        result = result.substring(0, result.indexOf(", "));
                    }
                }
            }
        }
        return result;
    }
}
