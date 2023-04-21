package org.universa.tcc.gemda.web.panel;

import java.util.List;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.universa.tcc.gemda.entidade.Sprint;
import org.universa.tcc.gemda.entidade.Story;
import org.universa.tcc.gemda.web.backlog.SprintBacklogPanel;
import org.universa.tcc.gemda.web.jquery.ui.tab.TabContent;

@SuppressWarnings("serial")
public class QuadroTarefaPanel extends TabContent {

    public QuadroTarefaPanel(Sprint sprint) {
        super("sprint-" + sprint.getId());
        add(new ListView<Story>("stories", (List<Story>) sprint.getStories()) {

            @Override
            protected void populateItem(ListItem<Story> item) {
                Story story = item.getModelObject();
                item.add(new StoryPanel(story));
            }
        });
        add(new SprintBacklogPanel(sprint.getStories()));
    }
}
