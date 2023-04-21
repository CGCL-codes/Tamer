package egovframework.com.cop.ems.service.impl;

import javax.annotation.Resource;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import egovframework.com.cop.ems.service.EgovMultiPartEmail;
import egovframework.com.cop.ems.service.EgovSndngMailService;
import egovframework.com.cop.ems.service.SndngMailVO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

/**
 * 메일 솔루션과 연동해서 이용해서 메일을 보내는 서비스 구현 클래스
 * @since 2011.09.09
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011.09.09  서준식       최초 작성
 *  2011.12.06  이기하       메일 첨부파일이 기능 추가 
 *  
 *  </pre>
 */
@Service("egovSndngMailService")
public class EgovSndngMailServiceImpl extends AbstractServiceImpl implements EgovSndngMailService {

    @Resource(name = "egovMultiPartEmail")
    private EgovMultiPartEmail egovMultiPartEmail;

    /** SndngMailRegistDAO */
    @Resource(name = "sndngMailRegistDAO")
    private SndngMailRegistDAO sndngMailRegistDAO;

    private static final Logger LOG = Logger.getLogger(EgovSndngMailServiceImpl.class.getClass());

    /**
	 * 메일을 발송한다
	 * @param vo SndngMailVO
	 * @return boolean
	 * @exception Exception
	 */
    public boolean sndngMail(SndngMailVO sndngMailVO) throws Exception {
        String recptnPerson = (sndngMailVO.getRecptnPerson() == null) ? "" : sndngMailVO.getRecptnPerson();
        String subject = (sndngMailVO.getSj() == null) ? "" : sndngMailVO.getSj();
        String emailCn = (sndngMailVO.getEmailCn() == null) ? "" : sndngMailVO.getEmailCn();
        String atchmnFileNm = (sndngMailVO.getOrignlFileNm() == null) ? "" : sndngMailVO.getOrignlFileNm();
        String atchmnFilePath = (sndngMailVO.getFileStreCours() == null) ? "" : sndngMailVO.getFileStreCours();
        try {
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(atchmnFilePath);
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("첨부파일입니다");
            attachment.setName(atchmnFileNm);
            egovMultiPartEmail.addTo(recptnPerson);
            egovMultiPartEmail.setSubject(subject);
            egovMultiPartEmail.setMsg(emailCn);
            egovMultiPartEmail.attach(attachment);
            egovMultiPartEmail.send();
        } catch (MailParseException ex) {
            sndngMailVO.setSndngResultCode("F");
            sndngMailRegistDAO.updateSndngMail(sndngMailVO);
            LOG.error("Sending Mail Exception : " + ex.getCause() + " [failure when parsing the message]");
            return false;
        } catch (MailAuthenticationException ex) {
            sndngMailVO.setSndngResultCode("F");
            sndngMailRegistDAO.updateSndngMail(sndngMailVO);
            LOG.error("Sending Mail Exception : " + ex.getCause() + " [authentication failure]");
            return false;
        } catch (MailSendException ex) {
            sndngMailVO.setSndngResultCode("F");
            sndngMailRegistDAO.updateSndngMail(sndngMailVO);
            LOG.error("Sending Mail Exception : " + ex.getCause() + " [failure when sending the message]");
            return false;
        } catch (Exception ex) {
            sndngMailVO.setSndngResultCode("F");
            sndngMailRegistDAO.updateSndngMail(sndngMailVO);
            LOG.error("Sending Mail Exception : " + ex.getCause() + " [unknown Exception]");
            return false;
        }
        return true;
    }
}
