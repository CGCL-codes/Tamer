package com.genITeam.ria.actions;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.genITeam.ria.bl.PersonalMessageBL;
import com.genITeam.ria.constant.Constants;
import com.genITeam.ria.exception.NGFException;
import com.genITeam.ria.utility.FormatConstants;
import com.genITeam.ria.utility.Utility;
import com.genITeam.ria.vo.Member;
import com.genITeam.ria.vo.PersonalMessage;

public class PersonalMessageAction extends AbstractAction {

    Logger logger = Logger.getLogger(PersonalMessageAction.class);

    /**
     * handlePersonalMessage used to get Parameters from Request object and write the resulting XML to Request 
     * 
     * @param request, response
     * 		 
     * @return none
     * @throws none
     */
    public void handlePersonalMessage(HttpServletRequest request, HttpServletResponse response) throws NGFException {
        System.out.println("handlePersonalMessage ");
        PersonalMessageBL personalMessageBL = new PersonalMessageBL();
        try {
            logger.info("start handlePersonalMessage");
            Member member = new Member();
            String actionType = request.getParameter("type");
            if (actionType.equalsIgnoreCase(Constants.ACTION_VIEW)) {
                int memberId = Integer.parseInt(request.getParameter("memberId"));
                member.setId(memberId);
                logger.debug("memberId = " + memberId);
                System.out.println("memberId = " + memberId);
                this.initAction(request, response);
                this.writeResponse(personalMessageBL.getPersonalMessage(member));
            } else if (actionType.equalsIgnoreCase(Constants.ACTION_ADD)) {
                Member memberSender = new Member();
                Member memberReceiver = new Member();
                String senderId = request.getParameter("senderId");
                String receiverName = request.getParameter("receiverId");
                int memberId = personalMessageBL.getReceiverIdByName(receiverName);
                String subject = request.getParameter("subject");
                String message = request.getParameter("message");
                memberSender.setId(Integer.parseInt(senderId));
                memberReceiver.setId(memberId);
                Date currentDate = Utility.convertStringToDate(Utility.getSQLTimeDate(), FormatConstants.MMDDYYhhmmssa_SEPARATOR_SLASH);
                PersonalMessage personalMessage = new PersonalMessage();
                personalMessage.setMemberByReceiverId(memberReceiver);
                personalMessage.setMemberBySenderId(memberSender);
                personalMessage.setSubject(subject);
                personalMessage.setMessage(message);
                personalMessage.setMessageDate(currentDate);
                personalMessageBL.savePersonalMessage(personalMessage);
                this.initAction(request, response);
                this.writeResponse("<message>Message Sent</message>");
            } else if (actionType.equalsIgnoreCase(Constants.ACTION_DELETE)) {
                String messageId = request.getParameter("messageId");
                PersonalMessage personalMessage = new PersonalMessage();
                personalMessage.setId(Integer.parseInt(messageId));
                personalMessageBL.deletePersonalMessage(personalMessage);
                this.initAction(request, response);
                this.writeResponse("<message>Message Deleted</message>");
            }
        } catch (NGFException e) {
            this.initAction(request, response);
            this.writeResponse("<error>" + e.getErrorMessage() + "</error>");
            System.out.print("Search Forum Excepton = " + e.getMessage());
        } catch (Exception e) {
            this.initAction(request, response);
            this.writeResponse("<error>" + e.getMessage() + "</error>");
            System.out.print("Search Forum Excepton = " + e.getMessage());
        }
    }
}
