package patho.textmining.receptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author  Reihs Robert
 * @author  Sauer Stefan
 * @version 2.0
 */
class ParserReceptorOestrogen implements ParserReceptor {

    private ReceptorEntry extracted_;

    private String diagnose_;

    Pattern pattern_ = null;

    /** Creates a new instance of ParserReceptorProgesteron */
    public ParserReceptorOestrogen() {
        String pattern = "OESTROGENREZEPTOR(EN|ENNACHWEIS)?[ :-]?[ ]?(\\w+)?[ ]?([\\(]SCORE[ ]?(\\d+)\\))?";
        pattern_ = Pattern.compile(pattern);
        extracted_ = new ReceptorEntry();
    }

    public void setUp(String text) {
        diagnose_ = text;
    }

    public ReceptorEntry code() {
        return CodingProgesteron();
    }

    public ReceptorEntry code(String text) {
        setUp(text);
        return CodingProgesteron();
    }

    public ReceptorEntry getReceptor() {
        return extracted_;
    }

    public String getType() {
        return new String("OESTROGEN");
    }

    private ReceptorEntry CodingProgesteron() {
        if (diagnose_ == null) return null;
        if (diagnose_.equals("")) return null;
        extracted_ = null;
        try {
            Matcher match = pattern_.matcher(diagnose_);
            while (match.find()) {
                String rezeptor_wert = "leer";
                String rezeptor_text = "";
                String rezeptor_bemerkung = "";
                boolean rezeptor_true = false;
                if (match.group(3) == null) {
                    if (match.group(2).equalsIgnoreCase("NEGATIV")) {
                        rezeptor_wert = "0";
                        rezeptor_text = "NEGATIV";
                        rezeptor_true = false;
                    } else {
                        if (match.group(2).equalsIgnoreCase("GERINGGRADIG")) {
                            rezeptor_wert = "2";
                            rezeptor_text = "GERINGGRADIG";
                            rezeptor_true = false;
                        } else {
                            if (match.group(2).equalsIgnoreCase("MITTELGRADIG")) {
                                rezeptor_wert = "4";
                                rezeptor_text = "MITTELGRADIG";
                                rezeptor_true = false;
                            } else {
                                if (match.group(2).equalsIgnoreCase("HOCHGRADIG")) {
                                    rezeptor_wert = "9";
                                    rezeptor_text = "HOCHGRADIG";
                                    rezeptor_true = false;
                                }
                            }
                        }
                    }
                } else {
                    rezeptor_wert = match.group(4);
                    if (rezeptor_wert.equalsIgnoreCase("0")) {
                        rezeptor_text = "NEGATIV";
                        rezeptor_true = true;
                    } else {
                        if (rezeptor_wert.equalsIgnoreCase("1")) {
                            rezeptor_text = "GERINGGRADIG";
                            rezeptor_true = true;
                        } else {
                            if (rezeptor_wert.equalsIgnoreCase("2")) {
                                rezeptor_text = "GERINGGRADIG";
                                rezeptor_true = true;
                            } else {
                                if (rezeptor_wert.equalsIgnoreCase("3")) {
                                    rezeptor_text = "GERINGGRADIG";
                                    rezeptor_true = true;
                                } else {
                                    if (rezeptor_wert.equalsIgnoreCase("4")) {
                                        rezeptor_text = "MITTELGRADIG";
                                        rezeptor_true = true;
                                    } else {
                                        if (rezeptor_wert.equalsIgnoreCase("5")) {
                                            rezeptor_text = "MITTELGRADIG";
                                            rezeptor_true = false;
                                        } else {
                                            if (rezeptor_wert.equalsIgnoreCase("6")) {
                                                rezeptor_text = "MITTELGRADIG";
                                                rezeptor_true = true;
                                            } else {
                                                if (rezeptor_wert.equalsIgnoreCase("7")) {
                                                    rezeptor_text = "HOCHGRADIG";
                                                    rezeptor_true = false;
                                                } else {
                                                    if (rezeptor_wert.equalsIgnoreCase("8")) {
                                                        rezeptor_text = "HOCHGRADIG";
                                                        rezeptor_true = true;
                                                    } else {
                                                        if (rezeptor_wert.equalsIgnoreCase("9")) {
                                                            rezeptor_text = "HOCHGRADIG";
                                                            rezeptor_true = true;
                                                        } else {
                                                            if (rezeptor_wert.equalsIgnoreCase("10")) {
                                                                rezeptor_text = "HOCHGRADIG";
                                                                rezeptor_true = false;
                                                            } else {
                                                                if (rezeptor_wert.equalsIgnoreCase("11")) {
                                                                    rezeptor_text = "HOCHGRADIG";
                                                                    rezeptor_true = false;
                                                                } else {
                                                                    if (rezeptor_wert.equalsIgnoreCase("12")) {
                                                                        rezeptor_text = "HOCHGRADIG";
                                                                        rezeptor_true = true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!rezeptor_wert.equalsIgnoreCase("leer")) {
                    extracted_.setValue(rezeptor_wert);
                    extracted_.setText(rezeptor_text);
                    extracted_.setComment(rezeptor_bemerkung);
                    extracted_.setIsvalid(rezeptor_true);
                    extracted_.setType(getType());
                }
            }
        } catch (Exception e) {
            System.err.println("TextMining::CodingOestrogenstatus: " + e.getMessage());
        }
        return extracted_;
    }
}
