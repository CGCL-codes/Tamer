package de.gee.erep.client.ui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import de.gee.erep.client.ui.displays.CountryDisplay;
import de.gee.erep.shared.entities.country.Country;

/**
 * @author Matthew Gee created: 13.03.2011
 */
public class CountryGridView implements CountryDisplay {

    /***/
    private final Grid grid;

    /***/
    private ArrayList<Country> list;

    /**
	 * @param eventBus 
	 * 
	 */
    public CountryGridView(final HandlerManager eventBus) {
        grid = new Grid();
        grid.setStyleName("cellTable");
    }

    /**
	 * @param list
	 *            l
	 */
    @Override
    public final void setData(final ArrayList<Country> list) {
        this.list = list;
        this.grid.clear();
        this.grid.resize(list.size() + 1, 6);
        int i = 1;
        setHeader();
        for (Country country : list) {
            setRow(country, i);
            i++;
        }
    }

    /**
	 * 
	 */
    private void setHeader() {
        this.grid.setWidget(0, 0, new Label("Flag"));
        Button idBt = new Button("ID");
        idBt.setStyleName("headerBt");
        this.grid.setWidget(0, 1, idBt);
        idBt.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                Collections.sort(CountryGridView.this.list, new Comparator<Country>() {

                    @Override
                    public int compare(final Country arg0, final Country arg1) {
                        return Integer.valueOf(arg0.getId()).compareTo(arg1.getId());
                    }
                });
                CountryGridView.this.setData(list);
            }
        });
        Button nameBt = new Button("Name");
        nameBt.setStyleName("headerBt");
        this.grid.setWidget(0, 2, nameBt);
        nameBt.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                Collections.sort(CountryGridView.this.list, new Comparator<Country>() {

                    @Override
                    public int compare(final Country arg0, final Country arg1) {
                        return arg0.getName().compareTo(arg1.getName());
                    }
                });
                CountryGridView.this.setData(list);
            }
        });
        Button citCountBt = new Button("CitCount");
        citCountBt.setStyleName("headerBt");
        this.grid.setWidget(0, 3, citCountBt);
        citCountBt.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                Collections.sort(CountryGridView.this.list, new Comparator<Country>() {

                    @Override
                    public int compare(final Country arg0, final Country arg1) {
                        return Integer.valueOf(arg0.getCitizenCount()).compareTo(arg1.getCitizenCount()) * -1;
                    }
                });
                CountryGridView.this.setData(list);
            }
        });
        Button avgLevelBt = new Button("AvgLevel");
        avgLevelBt.setStyleName("headerBt");
        this.grid.setWidget(0, 4, avgLevelBt);
        avgLevelBt.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                Collections.sort(CountryGridView.this.list, new Comparator<Country>() {

                    @Override
                    public int compare(final Country arg0, final Country arg1) {
                        return Integer.valueOf(arg0.getAverageCitizenLevel()).compareTo(arg1.getAverageCitizenLevel()) * -1;
                    }
                });
                CountryGridView.this.setData(list);
            }
        });
        Button regCount = new Button("RegionCount");
        regCount.setStyleName("headerBt");
        this.grid.setWidget(0, 5, regCount);
        regCount.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                Collections.sort(CountryGridView.this.list, new Comparator<Country>() {

                    @Override
                    public int compare(final Country arg0, final Country arg1) {
                        return Integer.valueOf(arg0.getRegionCount()).compareTo(arg1.getRegionCount()) * -1;
                    }
                });
                CountryGridView.this.setData(list);
            }
        });
    }

    /**
	 * @param country
	 *            c
	 * @param index
	 *            i
	 */
    private void setRow(final Country country, final int index) {
        this.grid.setWidget(index, 0, new Image("./Ressources/gfx/Flags_small/" + resolveFileName(country)));
        this.grid.setWidget(index, 1, new Label(String.valueOf(country.getId())));
        this.grid.setWidget(index, 2, new Label(country.getName()));
        this.grid.setWidget(index, 3, new Label(String.valueOf(country.getCitizenCount())));
        this.grid.setWidget(index, 4, new Label(String.valueOf(country.getAverageCitizenLevel())));
        this.grid.setWidget(index, 5, new Label(String.valueOf(country.getRegionCount())));
    }

    /**
	 * @param country
	 *            c
	 * @return s
	 */
    private String resolveFileName(final Country country) {
        String result = "";
        result = country.getName();
        result = result.replace(" and ", "-");
        result = result.replace(" ", "-");
        result = result.replace("(", "");
        result = result.replace(")", "");
        return result + ".gif";
    }

    @Override
    public final Widget asWidget() {
        return grid.asWidget();
    }
}
