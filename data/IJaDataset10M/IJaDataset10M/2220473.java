package com.hk.svr.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.bean.CmpActor;
import com.hk.bean.CmpActorCmt;
import com.hk.bean.CmpActorScore;
import com.hk.bean.Company;
import com.hk.bean.User;
import com.hk.svr.CmpActorCmtService;
import com.hk.svr.CmpActorScoreService;
import com.hk.svr.CmpActorService;
import com.hk.svr.CompanyService;
import com.hk.svr.UserService;

public class CmpActorCmtProcessor {

    @Autowired
    private CmpActorCmtService cmpActorCmtService;

    @Autowired
    private CmpActorService cmpActorService;

    @Autowired
    private UserService userService;

    @Autowired
    private CmpActorScoreService cmpActorScoreService;

    @Autowired
    private CompanyService companyService;

    public void createCmpActorCmt(CmpActorCmt cmpActorCmt, CmpActorScore cmpActorScore) {
        this.cmpActorCmtService.createCmpActorCmt(cmpActorCmt);
        this.cmpActorCmtService.updateCmpActorCmtScore(cmpActorCmt.getActorId(), cmpActorCmt.getUserId(), cmpActorScore.getScore());
        if (cmpActorScore.getScore() != 0) {
            this.saveCmpActorScore(cmpActorScore);
        }
    }

    public void saveCmpActorScore(CmpActorScore cmpActorScore) {
        long actorId = cmpActorScore.getActorId();
        long companyId = cmpActorScore.getCompanyId();
        this.cmpActorScoreService.saveCmpActorScore(cmpActorScore);
        this.cmpActorCmtService.updateCmpActorCmtScore(cmpActorScore.getActorId(), cmpActorScore.getUserId(), cmpActorScore.getScore());
        int actor_score = this.cmpActorScoreService.sumScoreByActorId(actorId);
        int actor_count = this.cmpActorScoreService.countCmpActorScoreByActorId(actorId);
        CmpActor cmpActor = this.cmpActorService.getCmpActor(actorId);
        cmpActor.setScoreUserNum(actor_count);
        cmpActor.setScore(actor_score);
        this.cmpActorService.updateCmpActor(cmpActor);
        int cmp_score = this.cmpActorService.sumCmpActorScoreByCompanyId(companyId);
        int cmp_count = this.cmpActorService.sumCmpActorScoreUserNumByCompanyId(companyId);
        Company company = this.companyService.getCompany(companyId);
        company.setTotalScore(cmp_score);
        company.setTotalVote(cmp_count);
        this.companyService.updateCompany(company);
    }

    public void updateCmpActorCmt(CmpActorCmt cmpActorCmt, CmpActorScore cmpActorScore) {
        this.cmpActorCmtService.updateCmpActorCmt(cmpActorCmt);
        this.cmpActorScoreService.saveCmpActorScore(cmpActorScore);
        this.cmpActorCmtService.updateCmpActorCmtScore(cmpActorCmt.getActorId(), cmpActorCmt.getUserId(), cmpActorScore.getScore());
        if (cmpActorScore.getScore() != 0) {
            this.saveCmpActorScore(cmpActorScore);
        }
    }

    public void deleteCmpActorCmt(CmpActorCmt cmpActorCmt) {
        this.cmpActorCmtService.deleteCmpActorCmt(cmpActorCmt.getCmtId());
        this.cmpActorScoreService.deleteCmpActorScoreByActorIdAndUserId(cmpActorCmt.getActorId(), cmpActorCmt.getUserId());
    }

    public List<CmpActorCmt> getCmpActorCmtListByCompanyId(long companyId, boolean buildUser, boolean buildActor, int begin, int size) {
        List<CmpActorCmt> list = this.cmpActorCmtService.getCmpActorCmtListByCompanyId(companyId, begin, size);
        if (buildUser) {
            this.buildUser(list);
        }
        if (buildActor) {
            this.buildActor(list);
        }
        return list;
    }

    public List<CmpActorCmt> getCmpActorCmtListByActorId(long actorId, boolean buildUser, boolean buildActor, int begin, int size) {
        List<CmpActorCmt> list = this.cmpActorCmtService.getCmpActorCmtListByActorId(actorId, begin, size);
        if (buildUser) {
            this.buildUser(list);
        }
        if (buildActor) {
            this.buildActor(list);
        }
        return list;
    }

    public List<CmpActorCmt> getCmpActorCmtList(boolean buildUser, boolean buildActor, int begin, int size) {
        List<CmpActorCmt> list = this.cmpActorCmtService.getCmpActorCmtList(begin, size);
        if (buildUser) {
            this.buildUser(list);
        }
        if (buildActor) {
            this.buildActor(list);
        }
        return list;
    }

    private void buildUser(List<CmpActorCmt> list) {
        List<Long> idList = new ArrayList<Long>();
        for (CmpActorCmt o : list) {
            idList.add(o.getUserId());
        }
        Map<Long, User> map = this.userService.getUserMapInId(idList);
        for (CmpActorCmt o : list) {
            o.setUser(map.get(o.getUserId()));
        }
    }

    private void buildActor(List<CmpActorCmt> list) {
        List<Long> idList = new ArrayList<Long>();
        for (CmpActorCmt o : list) {
            idList.add(o.getActorId());
        }
        Map<Long, CmpActor> map = this.cmpActorService.getCmpActorMapInId(idList);
        for (CmpActorCmt o : list) {
            o.setCmpActor(map.get(o.getActorId()));
        }
    }
}
