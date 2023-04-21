package org.shestkoff.nimium.model.entity;

import org.hibernate.annotations.BatchSize;
import javax.persistence.Entity;
import javax.persistence.Basic;

/**
 * Created by IntelliJ IDEA.
 * LightSpeed Arbitrage
 * User: Vasily
 * Date: 17.09.2009
 * Time: 15:21:56
 */
@Entity
@BatchSize(size = 1000)
public class StockGroup extends AbstractPersistentObject {

    @Basic
    private String nme;
}
