package org.internna.ossmoney.model;

import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import org.internna.ossmoney.model.Dashboard;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.jpa.entity.RooJpaEntity;
import org.springframework.roo.addon.serializable.RooSerializable;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import static org.springframework.util.StringUtils.hasText;

@RooJavaBean
@RooToString
@RooJpaEntity
@RooSerializable
@RooJpaActiveRecord
public class Widget extends AbstractEntity implements Comparable<Widget> {

    private static final long serialVersionUID = 7788708831012988910L;

    @NotNull
    private String url;

    private String configuration;

    @NotNull
    @Min(1L)
    @Max(5L)
    private Integer dashboardColumn;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Dashboard dashboard;

    @NotNull
    @Min(1L)
    @Max(5L)
    private Integer columnPosition;

    @NotNull
    private String title;

    @Override
    public final int compareTo(Widget widget) {
        return widget == null ? -1 : columnPosition.compareTo(widget.columnPosition);
    }

    public static Widget createInstance(String title, int column, int columnPosition, String url, String configuration) {
        Widget widget = new Widget();
        widget.setUrl(url);
        widget.setTitle(title);
        widget.setDashboardColumn(column);
        widget.setColumnPosition(columnPosition);
        widget.setConfiguration(configuration);
        return widget;
    }

    public String getConfiguredUrl() {
        return url + (hasText(configuration) ? configuration : "");
    }
}
