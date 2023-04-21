package com.sns2Life.user.business.service;

import java.util.List;
import com.sns2Life.user.persistence.model.SpaceInfo;
import com.sns2Life.user.persistence.vo.ContactsVO;
import com.sns2Life.user.persistence.vo.MemberBasicInfoVO;
import com.sns2Life.user.persistence.vo.PersonalInfosVO;

/**
 * @author HaydenWang
 * 
 */
public interface PersonalSettingService {

    MemberBasicInfoVO getBasicInformation(Integer uid);

    void saveBasicInformation(MemberBasicInfoVO bvo);

    List<SpaceInfo> getEducations(Integer uid);

    void saveEducationts(List<SpaceInfo> educations);

    List<SpaceInfo> getWorkExperiences(Integer uid);

    void saveWorkExperiences(List<SpaceInfo> workExperiences);

    ContactsVO getContacts(Integer uid);

    void saveContact(ContactsVO contactVO);

    PersonalInfosVO getPersonalInfos(Integer uid);

    void savePersonalInfos(PersonalInfosVO personalInfosVO);
}
