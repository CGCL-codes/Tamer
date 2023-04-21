package com.ploed.statsviewer;

import com.ploed.statsviewer.data.SortableEntityStatisticsDataProvider;
import com.ploed.statsviewer.model.EntityStats;
import com.ploed.statsviewer.model.StatisticsSummary;
import com.ploed.statsviewer.reader.StatisticsReader;
import com.ploed.statsviewer.reader.impl.StatisticsReaderJndi;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * User: mikeploed
 * Date: 19.12.2007
 * Time: 17:42:02
 */
public class EntityPanel extends StatsviewerPanel {

    public EntityPanel(String id) {
        super(id);
        StatisticsReader reader = new StatisticsReaderJndi();
        StatisticsSummary statisticsSummary = reader.retrieveConsolidatedStatistics();
        SortableEntityStatisticsDataProvider dp = new SortableEntityStatisticsDataProvider(statisticsSummary);
        final DataView dataView = new DataView("sorting", dp) {

            protected void populateItem(final Item item) {
                EntityStats es = (EntityStats) item.getModelObject();
                item.add(new Label("entity", es.getEntityName()));
                item.add(new Label("persistenceUnit", es.getPersistenceUnit()));
                item.add(new Label("loadCount", String.valueOf(es.getLoadCount())));
                item.add(new Label("fetchCount", String.valueOf(es.getFetchCount())));
                item.add(new Label("insertCount", String.valueOf(es.getInsertCount())));
                item.add(new Label("updateCount", String.valueOf(es.getUpdateCount())));
                item.add(new Label("deleteCount", String.valueOf(es.getDeleteCount())));
                item.add(new Label("optimisticFailureCount", String.valueOf(es.getOptimisticFailureCount())));
                item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {

                    public Object getObject() {
                        return (item.getIndex() % 2 == 1) ? "even" : "odd";
                    }
                }));
            }
        };
        dataView.setItemsPerPage(100);
        add(createOrderBy(dp, dataView, "getEntityName"));
        add(createOrderBy(dp, dataView, "getPersistenceUnit"));
        add(createOrderBy(dp, dataView, "getLoadCount"));
        add(createOrderBy(dp, dataView, "getInsertCount"));
        add(createOrderBy(dp, dataView, "getDeleteCount"));
        add(createOrderBy(dp, dataView, "getUpdateCount"));
        add(createOrderBy(dp, dataView, "getFetchCount"));
        add(createOrderBy(dp, dataView, "getOptimisticFailureCount"));
        add(dataView);
        add(new PagingNavigator("navigator", dataView));
    }
}
