package org.spantus.work.ui.container.panel;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import org.spantus.externals.recognition.bean.RecognitionResult;
import org.spantus.externals.recognition.bean.RecognitionResultDetails;
import org.spantus.work.ui.dto.SpantusWorkInfo;
import org.spantus.work.ui.i18n.I18nFactory;

/**
 *
 * @author mondhs
 */
public class RecognizeDetailDialog extends SpantusAboutDialog {

    private List<RecognitionResultDetails> results;

    public RecognizeDetailDialog(Frame owner) {
        super(owner);
        setTitle(I18nFactory.createI18n().getMessage("patternRecognized"));
        getJEditorPane().addHyperlinkListener(new RecognitionHyperlinkListener());
    }

    public void updateCtx(SpantusWorkInfo ctx, List<RecognitionResultDetails> results) {
        String css = "<head><style type=\"text/css\">" + ".label{font-weight:bold}" + "</style></head>";
        super.getJEditorPane().setText("<html>" + css + "<body><p>" + representResults(results) + "</p></body></html>");
        super.getJEditorPane().setCaretPosition(0);
        this.results = results;
    }

    private StringBuilder representResults(List<RecognitionResultDetails> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        for (RecognitionResult recognitionResult : results) {
            sb.append("<tr>");
            sb.append("<td>").append("<a href=\"").append(recognitionResult.getInfo().getId()).append("\">").append(recognitionResult.getInfo().getName()).append("</a></td>");
            sb.append("<td>").append(recognitionResult.getDistance()).append("</td>");
            sb.append("</tr>");
        }
        if (results.isEmpty()) {
            sb.append("<tr>");
            sb.append("<td>").append("No recognition pattern is found. Try to learn program some patterns first.").append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        return sb;
    }

    class RecognitionHyperlinkListener implements HyperlinkListener {

        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                StringTokenizer st = new StringTokenizer(e.getDescription(), " ");
                if (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    Long key = Long.valueOf(s);
                    for (RecognitionResultDetails recognitionResultDetails : results) {
                        if (recognitionResultDetails.getInfo().getId().equals(key)) {
                            Graphics2D g = (Graphics2D) getjLabel().getGraphics();
                            g.setColor(Color.white);
                            g.fillRect(0, 0, getjLabel().getHeight(), getjLabel().getWidth());
                            g.setColor(Color.red);
                            int[] xArr = new int[recognitionResultDetails.getPath().size()];
                            int[] yArr = new int[recognitionResultDetails.getPath().size()];
                            int i = 0;
                            for (Point p : recognitionResultDetails.getPath()) {
                                xArr[i] = p.x;
                                yArr[i] = p.y;
                                i++;
                            }
                            g.drawPolyline(xArr, yArr, xArr.length);
                            break;
                        }
                    }
                }
                e.getClass();
            }
        }
    }
}
