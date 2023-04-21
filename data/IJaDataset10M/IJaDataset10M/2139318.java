package com.jidesoft.spring.richclient.googledemo.view;

import com.google.soap.search.GoogleSearchResultElement;
import com.jidesoft.spring.richclient.googledemo.events.SearchResultsSelectionEvent;
import com.jidesoft.swing.StyleRange;
import com.jidesoft.swing.StyledLabel;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.docking.jide.view.JideAbstractView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;
import org.springframework.richclient.text.HtmlPane;
import javax.swing.*;
import java.awt.*;

/**
 * View to display properties via the JIDE property pane component
 * 
 * @author Jonny Wray
 *
 */
public class DetailsView extends JideAbstractView implements ApplicationListener {

    private JLabel title = new JLabel(" ");

    private StyledLabel url = new StyledLabel(" ");

    private HtmlPane snippit = new HtmlPane();

    private static final Color GOOGLE_GREEN = new Color(40, 180, 40);

    private PrintCommandExecutor printCommandExecutor = new PrintCommandExecutor();

    private static final String PRINT_COMMAND_ID = "printCommand";

    public DetailsView() {
    }

    protected void registerLocalCommandExecutors(PageComponentContext context) {
        context.register(PRINT_COMMAND_ID, printCommandExecutor);
    }

    protected JComponent createControl() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        snippit.setEditable(false);
        snippit.setAntiAlias(true);
        JScrollPane scrollPane = new JScrollPane(snippit, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        url.setStyleRanges(new StyleRange[] { new StyleRange(GOOGLE_GREEN) });
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(url, BorderLayout.SOUTH);
        return panel;
    }

    /**
	 * Listens for search results selection event. These events should occur on the EDT
	 * but I check to make sure.
	 */
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof SearchResultsSelectionEvent) {
            final SearchResultsSelectionEvent selectionEvent = (SearchResultsSelectionEvent) event;
            if (SwingUtilities.isEventDispatchThread()) {
                updateView(selectionEvent);
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        updateView(selectionEvent);
                    }
                });
            }
        }
    }

    private void updateView(SearchResultsSelectionEvent selectionEvent) {
        GoogleSearchResultElement element = selectionEvent.getSearchResult();
        title.setText(getTitleHtmlText(element.getTitle()));
        snippit.setText(wrapInHtmlTags(element.getSnippet()));
        String urlText = element.getURL() + " - " + element.getCachedSize();
        url.setText(urlText);
        url.setToolTipText(urlText);
    }

    private String getTitleHtmlText(String base) {
        StringBuffer builder = new StringBuffer();
        builder.append("<html>");
        builder.append("<h3 style=\"color: blue;\">");
        builder.append(base);
        builder.append("</h3>");
        builder.append("</html>");
        return builder.toString();
    }

    private String wrapInHtmlTags(String text) {
        StringBuffer builder = new StringBuffer();
        builder.append("<html>");
        builder.append("<head></head>");
        builder.append("<body><p>");
        builder.append(text);
        builder.append("</p></body>");
        builder.append("</html>");
        return builder.toString();
    }

    private class PrintCommandExecutor extends AbstractActionCommandExecutor {

        public boolean isEnabled() {
            return true;
        }

        public void execute() {
            String message = "I'm sorry, printing has not yet been implemented";
            JOptionPane.showMessageDialog(null, message, "Printing Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
