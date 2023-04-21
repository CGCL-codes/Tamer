package com.zpyr.mvc.interfaces;

import java.util.HashMap;
import java.util.List;
import com.zpyr.common.exception.SelectException;
import com.zpyr.mvc.vo.AddrList;
import com.zpyr.mvc.vo.Carc_category_info;
import com.zpyr.mvc.vo.Carc_evaluation;
import com.zpyr.mvc.vo.Carc_info;
import com.zpyr.mvc.vo.Carc_member;
import com.zpyr.mvc.vo.Carc_user_info;

public interface CarcMapper {

    public Carc_info getCarc_info(String info_seq);

    public List<Carc_info> getList_carc_info(HashMap<String, Object> parameters);

    public List<Carc_user_info> getList_carc_user_info(HashMap<String, Object> parameters);

    public int insertCarc_info(HashMap<String, Object> parameters);

    public List<Carc_evaluation> getList_carc_evaluation(HashMap<String, Object> parameters);

    public int doInsertEvaluation(HashMap<String, Object> parameters);

    public int insertTest(int field1);

    public int insertError();

    public List<Carc_info> searchAroundByDistance(HashMap<String, Object> parameters);

    public List<Carc_info> searchName(HashMap<String, Object> parameters);

    public List<String> selectSiDoList();

    public List<String> selectGuBunList(String addr_sido);

    public List<String> selectDongList(HashMap<String, Object> parameters);

    public List<Carc_info> searchList(HashMap<String, Object> parameters);

    public int updateGrade(HashMap<String, Object> parameters);

    public int copyUserToInfo(String user_info_seq);

    public int deleteCarcUserInfo(String user_info_seq);

    public List<AddrList> selectAddrList();

    public List<Carc_category_info> selectCategoryList(HashMap<String, Object> parameters);

    public int insertCarcUserInfo(HashMap<String, Object> parameters);

    public int insertMember(HashMap<String, Object> parameters);

    public Carc_member selectMember(HashMap<String, Object> parameters);

    public List<Carc_evaluation> getList_eval_info(HashMap<String, Object> parameters);
}
