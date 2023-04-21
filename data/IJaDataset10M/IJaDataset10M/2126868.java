package com.incendiaryblue.cmslite;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.NamedQuery;

/**
 * Working content int entity
 * @author guy
 */
@Entity
@Table(name = "wc_data_int")
@NamedQuery(name = "getDataInt_working", query = "SELECT wcDataInt FROM WcDataInt wcDataInt " + "WHERE wcDataInt.dataPK.contentId =:contentIdParam")
public class WcDataInt extends DataInt {
}
