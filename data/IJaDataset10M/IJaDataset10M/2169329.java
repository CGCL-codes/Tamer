package org.chess.quasimodo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import org.chess.quasimodo.application.ApplicationContextAdapter;
import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.event.NotationChangedAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component("notationPanel")
public class NotationPanel extends JPanel implements NotationChangedAware {

    @Autowired
    private QuasimodoContext context;

    @Autowired
    private EventPublisherAdapter eventPublisher;

    @Autowired
    private ApplicationContextAdapter contextAdapter;

    private JScrollPane scrollPane;

    private JTextPane notationTextPane;

    private JTextPane materialTextPane;

    public NotationPanel() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));
        add(getMaterialTextPane(), BorderLayout.SOUTH);
        add(getScrollPane(), BorderLayout.CENTER);
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBorder(null);
            scrollPane.setViewportView(getNotationTextPane());
        }
        return scrollPane;
    }

    private JTextPane getMaterialTextPane() {
        if (materialTextPane == null) {
            materialTextPane = new JTextPane();
            materialTextPane.setEditable(false);
            materialTextPane.setPreferredSize(new Dimension(6, 50));
            materialTextPane.setRequestFocusEnabled(false);
            materialTextPane.setFocusable(false);
        }
        return materialTextPane;
    }

    protected JTextPane getNotationTextPane() {
        if (notationTextPane == null) {
            notationTextPane = new JTextPane();
            notationTextPane.setEditable(false);
            notationTextPane.addHyperlinkListener(new HyperlinkListener() {

                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == EventType.ACTIVATED) {
                        if (e.getURL() != null) {
                            if (!context.hasActiveGame()) {
                                CommandEvent goToMove = new CommandEvent(this, CommandEvent.Command.GO_TO_MOVE);
                                goToMove.addParameter("move", e.getURL().getPath());
                                eventPublisher.publishEvent(goToMove);
                            }
                        } else {
                            Highlighter highlighter = notationTextPane.getHighlighter();
                            highlighter.removeAllHighlights();
                            Element source = e.getSourceElement();
                            try {
                                highlighter.addHighlight(source.getStartOffset(), source.getEndOffset(), new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY));
                            } catch (BadLocationException e1) {
                                e1.printStackTrace();
                            }
                            notationTextPane.setCaretPosition(source.getStartOffset());
                        }
                    }
                }
            });
            notationTextPane.setContentType("text/html");
        }
        return notationTextPane;
    }

    public void notationContentChanged(String content, HyperlinkEvent event) {
        notationTextPane.setText(content);
        notationTextPane.requestFocusInWindow();
        notationTextPane.fireHyperlinkUpdate(event);
    }
}
