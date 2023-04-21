package org.tolven.growthchart;

import java.util.List;
import org.tolven.growthchart.entity.Lenageinf;
import org.tolven.growthchart.entity.Statage;
import org.tolven.growthchart.entity.Wtage;
import org.tolven.growthchart.entity.Wtageinf;

/**
 * To manage Growth Chart
 * 
 * @author Suja
 * added on 01/20/2011
 */
public interface GrowthChartLocal {

    /**
	 * Get all WTAGE data
	 * @param gender
	 * @return List<Wtage>
	 */
    public List<Wtage> findWtageData(int gender);

    /**
	 * Get all WTAGEINF data
	 * @param gender
	 * @return List<Wtageinf>
	 */
    public List<Wtageinf> findWtageinfData(int gender);

    /**
	 * Get all LENAGEINF data
	 * @param gender
	 * @return List<Lenageinf>
	 */
    public List<Lenageinf> findLenageinfData(int gender);

    /**
	 * Get all STATAGE data
	 * @param gender
	 * @return List<Statage>
	 */
    public List<Statage> findStatageData(int gender);

    /**
	 * Add an entry in Lenageinf table
	 * @param Lenageinf
	 * @return 
	 */
    public void addLenageinf(Lenageinf lenageinf);

    /**
	 * Add an entry in Statage table
	 * @param Statage
	 * @return 
	 */
    public void addStatage(Statage statage);

    /**
	 * Add an entry in Wtage table
	 * @param Wtage
	 * @return 
	 */
    public void addWtage(Wtage wtage);

    /**
	 * Add an entry in Wtageinf table
	 * @param Wtageinf
	 * @return 
	 */
    public void addWtageinf(Wtageinf wtageinf);

    /**
	 * Remove all entries from Lenageinf table
	 * @param 
	 * @return 
	 */
    public void clearLenageinf();

    /**
	 * Remove all entries from Statage table
	 * @param 
	 * @return 
	 */
    public void clearStatage();

    /**
	 * Remove all entries from Wtage table
	 * @param 
	 * @return 
	 */
    public void clearWtage();

    /**
	 * Remove all entries from Wtageinf table
	 * @param 
	 * @return 
	 */
    public void clearWtageinf();

    /**
	 * Load Growth Chart Entries
	 * @param xml - XML Data from the restful api
	 * @return 
	 */
    public void loadGrowthChart(String xml);
}
