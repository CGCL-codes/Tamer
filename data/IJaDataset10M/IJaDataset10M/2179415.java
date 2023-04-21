package jpicedt.graphic.io.parser;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.StringReader;
import jpicedt.graphic.io.util.UserDataCommentedLine;
import jpicedt.graphic.io.util.UserDataVerbatimLine;

/**
 * Cette classe permet d'extraire le JPIC-XML enfoui dans un fichier de
 * sauvegarde jPicEdt dont le format est connu (ou suppos� l'�tre), et tel que
 * l'enfouissement utilise un m�thode g�n�rique consistant � mettre le code
 * JPIC-XML en commentaire, avec chaque commentaire d�limit� par une marque de
 * d�part et une marque de fin ligne par ligne.
 *@author <a href="mailto:vincentb1@users.sourceforge.net">Vincent Bela�che</a>
 *@since jPicEdt 1.6
 */
public class JPICXmlFormatExtractor extends JPICXmlExtractor implements JPICXmlExtraction {

    String formatName;

    String startCommentRegexp;

    String endCommentRegexp;

    boolean isCommentedXml;

    int lineCountPerMetaLine;

    /**
	 *@param formatName nom du format tel qu'il appara�t dans la ligne "Created by &hellip;"
	 *@param startCommentRegexp expression rationnelle marquant le d�but d'un
	 *commentaire.
	 *@param endCommentRegexp expression rationnelle marquant la fin d'un commentaire.
	 *@param isCommentedXml true si le code JPIC-XML appara�t en commentaire
	 *dans le fichier, o� les commentaires sont selon le format correspondant
	 *� l'extracteur implantant l'interface.
	 *@param lineCountPerMetaLine le fichier est d�coup� en m�ta-ligne, chaque
	 *m�ta ligne �tant  bloc de lineCountPerMetaLine lignes.
	 *@since jPicEdt 1.6
	 */
    public JPICXmlFormatExtractor(String formatName, String startCommentRegexp, String endCommentRegexp, boolean isCommentedXml, int lineCountPerMetaLine) {
        this.formatName = formatName;
        this.startCommentRegexp = startCommentRegexp;
        this.endCommentRegexp = endCommentRegexp;
        this.isCommentedXml = isCommentedXml;
        this.lineCountPerMetaLine = lineCountPerMetaLine;
    }

    /**
	 * Extrait le code JPIC-XML enfoui, s'il en est, dans un fichier au format
	 * TeX.
	 *@param inputString tampon contenant le fichier o� chercher le code
	 *JPIC-XML enfoui.
	 *@return null si pas de JPIC-XML trouv�, un descripteur ExtractedXml
	 *du fichier sinon.
	 *@since jPicEdt 1.6
	 */
    public ExtractedXml extractXml(String inputString) {
        StringBuilder extractedXmlText = new StringBuilder(100);
        ExtractedXml extractedXml = new ExtractedXml();
        BufferedReader input = new BufferedReader(new StringReader(inputString));
        Pattern createdPattern = Pattern.compile("^" + startCommentRegexp + XML_HEAD_MARK_REGEXP + endCommentRegexp + "$");
        Pattern commentedLinePattern = Pattern.compile("^" + startCommentRegexp + "(.*)" + endCommentRegexp + "$");
        Pattern beginPattern = Pattern.compile("^" + startCommentRegexp + XML_BEGIN_MARK_REGEXP + endCommentRegexp + "$");
        Pattern endPattern = Pattern.compile("^" + startCommentRegexp + XML_END_MARK_REGEXP + endCommentRegexp + "$");
        Pattern userStartPattern = Pattern.compile("^" + startCommentRegexp + USER_START_MARK_REGEXP + endCommentRegexp + "$");
        String line = null;
        StringBuilder metaLineSB = new StringBuilder();
        String metaLine;
        int state = 0;
        try {
            for (; ; ) {
                metaLineSB.setLength(0);
                for (int i = 0; i < lineCountPerMetaLine; ++i) {
                    if ((line = input.readLine()) != null) {
                        if (i > 0) metaLineSB.append('\n');
                        metaLineSB.append(line);
                    } else if (i == 0 && state >= 3) {
                        extractedXml.extractedCodedText = extractedXmlText.toString();
                        return extractedXml;
                    } else return null;
                }
                metaLine = metaLineSB.toString();
                Matcher m;
                switch(state) {
                    case 0:
                        {
                            m = createdPattern.matcher(metaLine);
                            if (m.matches()) {
                                state = 1;
                                extractedXml.version = m.group(XML_HEAD_MARK_VERSION_GROUP);
                                if (!m.group(XML_HEAD_MARK_FORMAT_GROUP).equals(formatName)) return null;
                            } else {
                                m = commentedLinePattern.matcher(metaLine);
                                if (m.matches()) extractedXml.preambleUserData.add(new UserDataCommentedLine(m.group(1))); else extractedXml.preambleUserData.add(new UserDataVerbatimLine(metaLine));
                            }
                            break;
                        }
                    case 1:
                        {
                            m = beginPattern.matcher(metaLine);
                            if (m.matches()) state = 2;
                            break;
                        }
                    case 2:
                        {
                            m = endPattern.matcher(metaLine);
                            if (m.matches()) state = 3; else {
                                if (isCommentedXml) {
                                    m = commentedLinePattern.matcher(metaLine);
                                    if (m.matches()) {
                                        extractedXmlText.append(m.group(1));
                                    } else return null;
                                } else {
                                    extractedXmlText.append(metaLine);
                                }
                                extractedXmlText.append('\n');
                            }
                            break;
                        }
                    case 3:
                        {
                            m = userStartPattern.matcher(metaLine);
                            if (m.matches()) {
                                state = 4;
                            } else {
                                extractedXml.needsEncoding = false;
                            }
                            break;
                        }
                    case 4:
                        {
                            m = commentedLinePattern.matcher(metaLine);
                            if (m.matches()) extractedXml.postambleUserData.add(new UserDataCommentedLine(m.group(1))); else extractedXml.postambleUserData.add(new UserDataVerbatimLine(metaLine));
                            break;
                        }
                }
            }
        } catch (IOException ioEx) {
            return null;
        }
    }
}

;
