package egovframework.com.uat.uia.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import egovframework.com.cmm.LoginVO;
import egovframework.com.cop.ems.service.EgovSndngMailRegistService;
import egovframework.com.cop.ems.service.SndngMailVO;
import egovframework.com.uat.uia.service.EgovLoginService;
import egovframework.com.utl.fcc.service.EgovNumberUtil;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 일반 로그인, 인증서 로그인을 처리하는 비즈니스 구현 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.06
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.06  박지욱          최초 생성 
 *  2011.08.26  서준식          EsntlId를 이용한 로그인 추가
 *  </pre>
 */
@Service("loginService")
public class EgovLoginServiceImpl extends AbstractServiceImpl implements EgovLoginService {

    @Resource(name = "loginDAO")
    private LoginDAO loginDAO;

    /** EgovSndngMailRegistService */
    @Resource(name = "sndngMailRegistService")
    private EgovSndngMailRegistService sndngMailRegistService;

    /**
     * 2011.08.26
	 * EsntlId를 이용한 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public LoginVO actionLoginByEsntlId(LoginVO vo) throws Exception {
        LoginVO loginVO = loginDAO.actionLoginByEsntlId(vo);
        if (loginVO != null && !loginVO.getId().equals("") && !loginVO.getPassword().equals("")) {
            return loginVO;
        } else {
            loginVO = new LoginVO();
        }
        return loginVO;
    }

    /**
	 * 일반 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public LoginVO actionLogin(LoginVO vo) throws Exception {
        String enpassword = EgovFileScrty.encryptPassword(vo.getPassword());
        vo.setPassword(enpassword);
        LoginVO loginVO = loginDAO.actionLogin(vo);
        if (loginVO != null && !loginVO.getId().equals("") && !loginVO.getPassword().equals("")) {
            return loginVO;
        } else {
            loginVO = new LoginVO();
        }
        return loginVO;
    }

    /**
	 * 인증서 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public LoginVO actionCrtfctLogin(LoginVO vo) throws Exception {
        LoginVO loginVO = loginDAO.actionCrtfctLogin(vo);
        if (loginVO != null && !loginVO.getId().equals("") && !loginVO.getPassword().equals("")) {
            return loginVO;
        } else {
            loginVO = new LoginVO();
        }
        return loginVO;
    }

    /**
	 * 아이디를 찾는다.
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public LoginVO searchId(LoginVO vo) throws Exception {
        LoginVO loginVO = loginDAO.searchId(vo);
        if (loginVO != null && !loginVO.getId().equals("")) {
            return loginVO;
        } else {
            loginVO = new LoginVO();
        }
        return loginVO;
    }

    /**
	 * 비밀번호를 찾는다.
	 * @param vo LoginVO
	 * @return boolean
	 * @exception Exception
	 */
    public boolean searchPassword(LoginVO vo) throws Exception {
        boolean result = true;
        LoginVO loginVO = loginDAO.searchPassword(vo);
        if (loginVO == null || loginVO.getPassword() == null || loginVO.getPassword().equals("")) {
            return false;
        }
        String newpassword = "";
        for (int i = 1; i <= 6; i++) {
            if (i % 3 != 0) {
                newpassword += EgovStringUtil.getRandomStr('a', 'z');
            } else {
                newpassword += EgovNumberUtil.getRandomNum(0, 9);
            }
        }
        LoginVO pwVO = new LoginVO();
        String enpassword = EgovFileScrty.encryptPassword(newpassword);
        pwVO.setId(vo.getId());
        pwVO.setPassword(enpassword);
        pwVO.setUserSe(vo.getUserSe());
        loginDAO.updatePassword(pwVO);
        SndngMailVO sndngMailVO = new SndngMailVO();
        sndngMailVO.setDsptchPerson("webmaster");
        sndngMailVO.setRecptnPerson(vo.getEmail());
        sndngMailVO.setSj("[MOPAS] 임시 비밀번호를 발송했습니다.");
        sndngMailVO.setEmailCn("고객님의 임시 비밀번호는 " + newpassword + " 입니다.");
        sndngMailVO.setAtchFileId("");
        result = sndngMailRegistService.insertSndngMail(sndngMailVO);
        return result;
    }
}
