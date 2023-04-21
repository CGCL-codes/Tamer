package org.s3b.search.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author  Lukasz Porwol
 *
 */
public class BoostFactorKeeper {

    public static Double foafBoost;

    public static Double taxBoost;

    public static Double wordnetBoost;

    public static Double authorHitBoost;

    public static Double recentHitBoost;

    public static Double oldHitBoost;

    public static Integer recentDays;

    static {
        Properties props = new Properties();
        try {
            InputStream bis = BoostFactorKeeper.class.getResourceAsStream("/s3b_search.properties");
            props.load(bis);
            try {
                foafBoost = Double.parseDouble(props.getProperty("foafratio"));
            } catch (NumberFormatException e) {
                foafBoost = 1.0D;
            }
            try {
                taxBoost = Double.parseDouble(props.getProperty("taxratio"));
            } catch (NumberFormatException e) {
                taxBoost = 1.0D;
            }
            try {
                wordnetBoost = Double.parseDouble(props.getProperty("wordnetratio"));
            } catch (NumberFormatException e) {
                wordnetBoost = 1.0D;
            }
            try {
                authorHitBoost = Double.parseDouble(props.getProperty("author"));
            } catch (NumberFormatException e) {
                authorHitBoost = 1.0D;
            }
            try {
                recentHitBoost = Double.parseDouble(props.getProperty("recent"));
            } catch (NumberFormatException e) {
                recentHitBoost = 1.0D;
            }
            try {
                oldHitBoost = Double.parseDouble(props.getProperty("old"));
            } catch (NumberFormatException e) {
                oldHitBoost = 1.0D;
            }
            try {
                recentDays = Integer.parseInt(props.getProperty("recentDays"));
            } catch (NumberFormatException e) {
                recentDays = 10;
            }
        } catch (IOException e) {
            throw new RuntimeException("BoostFactorKeeper - cannot read configuration s3b_search.properties");
        }
    }
}
