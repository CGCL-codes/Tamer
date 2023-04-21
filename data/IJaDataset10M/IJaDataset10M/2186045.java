package com.nccsjz.back.faq.service;

import java.sql.SQLException;
import java.util.List;
import com.nccsjz.base.BaseService;
import com.nccsjz.pojo.Faq;
import com.nccsjz.back.faq.dao.FaqDAO;
import com.nccsjz.utils.Pager;

/**
 * 用于处理新闻相关的业务逻辑
 * ＠author stone
 * @version 1.0
 * */
public class FaqService extends BaseService {

    public Faq getFaq(long id) throws SQLException {
        Faq faq = null;
        try {
            openCon();
            faq = new FaqDAO(con).getFaq(id);
        } finally {
            closeCon();
        }
        return faq;
    }

    public boolean addFaq(Faq Faq) throws SQLException {
        boolean flag = false;
        try {
            openCon();
            flag = new FaqDAO(con).addFaq(Faq);
        } finally {
            closeCon();
        }
        return flag;
    }

    public boolean deleteFaq(long id) throws SQLException {
        boolean flag = false;
        try {
            openCon();
            flag = new FaqDAO(con).deleteFaq(id);
        } finally {
            closeCon();
        }
        return flag;
    }

    public Pager getFaq(int pageSize, int pageNo) throws SQLException {
        Pager pager = null;
        try {
            openCon();
            pager = new FaqDAO(con).getFaq(pageSize, pageNo);
        } finally {
            closeCon();
        }
        return pager;
    }

    public boolean updateFaq(Faq Faq) throws SQLException {
        boolean flag = false;
        try {
            openCon();
            flag = new FaqDAO(con).updateFaq(Faq);
        } finally {
            closeCon();
        }
        return flag;
    }

    public boolean deleteBatchFaq(List<Long> list) throws SQLException {
        boolean flag = false;
        try {
            openCon();
            con.setAutoCommit(false);
            flag = new FaqDAO(con).deleteBathFaq(list);
            con.commit();
        } finally {
            closeCon();
        }
        return flag;
    }

    /**
	  * 得到前n条新闻。
	  * @return
	  */
    public List<Faq> getTopN(int n) {
        List<Faq> Faq = null;
        try {
            openCon();
            Faq = new FaqDAO(con).getTopNFaqs(n);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeCon();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Faq;
    }
}
