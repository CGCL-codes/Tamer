package com.sns2Life.user.business.service;

import com.sns2Life.common.business.util.BusinessException;
import com.sns2Life.common.business.vo.LogInMemberVO;
import com.sns2Life.common.business.vo.LogedInMemberVO;
import com.sns2Life.user.persistence.model.Member;
import com.sns2Life.user.persistence.vo.MemberRegisterVO;

public interface SecurityService {

    public Boolean check4Register(MemberRegisterVO vo) throws BusinessException;

    public LogedInMemberVO register(MemberRegisterVO vo) throws BusinessException;

    public LogedInMemberVO login(LogInMemberVO vo) throws BusinessException;

    public LogedInMemberVO login(String userName, String password, String ip) throws BusinessException;

    public LogedInMemberVO login(String userName, String password, Integer ip) throws BusinessException;

    public Member checkPassword(Integer uid, String password) throws BusinessException;

    public Member checkPassword(String userName, String password) throws BusinessException;
}
