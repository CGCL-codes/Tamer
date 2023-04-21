package com.vlee.ejb.customer;

import java.math.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.BigDecimal;
import java.io.Serializable;
import com.vlee.util.*;
import com.vlee.ejb.inventory.*;

public class CustMembershipCampaignRulesObject extends java.lang.Object implements Serializable {

    public Integer pkid;

    public String namespace;

    public Integer index_id;

    public Integer sequence;

    public String rule_type;

    public String option1;

    public String option2;

    public String option3;

    public String filter_item_option;

    public String filter_item_conditions;

    public String calc_option;

    public BigDecimal calc_point_ratio;

    public BigDecimal calc_point_rounding;

    public BigDecimal calc_point_absolute;

    public String calc_point_max_logic;

    public BigDecimal calc_point_value_max;

    public String calc_point_min_logic;

    public BigDecimal calc_point_value_min;

    public String cash_topup_option;

    public BigDecimal cash_topup_price_ratio;

    public BigDecimal cash_topup_rounding;

    public BigDecimal cash_topup_absolute;

    public String cash_topup_max_logic;

    public BigDecimal cash_topup_value_max;

    public String cash_topup_min_logic;

    public BigDecimal cash_topup_value_min;

    public String item_code;

    public String item_value;

    public String category0_logic;

    public String category0_value;

    public String category1_logic;

    public String category1_value;

    public String category2_logic;

    public String category2_value;

    public String category3_logic;

    public String category3_value;

    public String category4_logic;

    public String category4_value;

    public String category5_logic;

    public String category5_value;

    public String guid;

    public CustMembershipCampaignRulesObject() {
        this.pkid = new Integer(0);
        this.index_id = new Integer(0);
        this.sequence = new Integer(0);
        this.rule_type = "";
        this.option1 = "";
        this.option2 = "";
        this.option3 = "";
        this.filter_item_option = "";
        this.filter_item_conditions = "";
        this.calc_option = "";
        this.calc_point_ratio = new BigDecimal(0);
        this.calc_point_rounding = new BigDecimal(0);
        this.calc_point_absolute = new BigDecimal(0);
        this.calc_point_max_logic = "";
        this.calc_point_value_max = new BigDecimal(0);
        this.calc_point_min_logic = "";
        this.calc_point_value_min = new BigDecimal(0);
        this.cash_topup_option = "";
        this.cash_topup_price_ratio = new BigDecimal(0);
        this.cash_topup_rounding = new BigDecimal(0);
        this.cash_topup_absolute = new BigDecimal(0);
        this.cash_topup_max_logic = "";
        this.cash_topup_value_max = new BigDecimal(0);
        this.cash_topup_min_logic = "";
        this.cash_topup_value_min = new BigDecimal(0);
        this.item_code = "";
        this.item_value = "";
        this.category0_logic = "";
        this.category0_value = "";
        this.category1_logic = "";
        this.category1_value = "";
        this.category2_logic = "";
        this.category2_value = "";
        this.category3_logic = "";
        this.category3_value = "";
        this.category4_logic = "";
        this.category4_value = "";
        this.category5_logic = "";
        this.category5_value = "";
        this.guid = "";
        try {
            GUIDGenerator guidGen = new GUIDGenerator();
            this.guid = guidGen.getUUID();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    public Integer getSequence() {
        return this.sequence;
    }

    public BigDecimal calculatePoints(BigDecimal price, BigDecimal qty) {
        BigDecimal bdDefault = new BigDecimal(0);
        if (CustMembershipCampaignRulesBean.CALC_OPTION_ABSOLUTE.equals(this.calc_option)) {
            return this.calc_point_absolute.multiply(qty);
        }
        if (CustMembershipCampaignRulesBean.CALC_OPTION_RATIO.equals(this.calc_option)) {
            BigDecimal points = this.calc_point_ratio.multiply(price).multiply(qty);
            if (CustMembershipCampaignRulesBean.CALC_POINT_LIMIT.equals(this.calc_point_min_logic)) {
                points = points.max(this.calc_point_value_min.multiply(qty));
            }
            if (CustMembershipCampaignRulesBean.CALC_POINT_LIMIT.equals(this.calc_point_max_logic)) {
                points = points.min(this.calc_point_value_max.multiply(qty));
            }
            return points;
        }
        return bdDefault;
    }

    public BigDecimal calculateTopup(BigDecimal price, BigDecimal qty) {
        BigDecimal bdDefault = new BigDecimal(0);
        if (CustMembershipCampaignRulesBean.CALC_OPTION_ABSOLUTE.equals(this.cash_topup_option)) {
            return this.cash_topup_absolute.multiply(qty);
        }
        if (CustMembershipCampaignRulesBean.CALC_OPTION_RATIO.equals(this.cash_topup_option)) {
            BigDecimal amount = this.cash_topup_price_ratio.multiply(price).multiply(qty);
            if (CustMembershipCampaignRulesBean.CALC_POINT_LIMIT.equals(this.cash_topup_min_logic)) {
                amount = amount.max(this.cash_topup_value_min.multiply(qty));
            }
            if (CustMembershipCampaignRulesBean.CALC_POINT_LIMIT.equals(this.cash_topup_max_logic)) {
                amount = amount.min(this.cash_topup_value_max.multiply(qty));
            }
            return amount;
        }
        return bdDefault;
    }
}
