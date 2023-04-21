package br.com.mcampos.ejb.core.util;

import br.com.mcampos.dto.anoto.AnotoPageFieldDTO;
import br.com.mcampos.dto.anoto.FormDTO;
import br.com.mcampos.dto.anoto.PgcAttachmentDTO;
import br.com.mcampos.dto.system.SystemParametersDTO;
import br.com.mcampos.dto.user.CompanyDTO;
import br.com.mcampos.dto.user.attributes.CompanyPositionDTO;
import br.com.mcampos.dto.user.attributes.CompanyTypeDTO;
import br.com.mcampos.dto.user.login.AccessLogTypeDTO;
import br.com.mcampos.dto.user.login.ListLoginDTO;
import br.com.mcampos.dto.user.login.LoginDTO;
import br.com.mcampos.ejb.cloudsystem.anoto.form.AnotoForm;
import br.com.mcampos.ejb.cloudsystem.anoto.page.AnotoPageUtil;
import br.com.mcampos.ejb.cloudsystem.anoto.page.field.entity.AnotoPageField;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.PgcPageUtil;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.attachment.PgcPageAttachment;
import br.com.mcampos.ejb.cloudsystem.security.accesslog.AccessLogType;
import br.com.mcampos.ejb.cloudsystem.system.fieldtype.FieldTypeUtil;
import br.com.mcampos.ejb.cloudsystem.user.attribute.companyposition.entity.CompanyPosition;
import br.com.mcampos.ejb.cloudsystem.user.attribute.companytype.CompanyType;
import br.com.mcampos.ejb.cloudsystem.user.attribute.userstatus.UserStatusUtil;
import br.com.mcampos.ejb.cloudsystem.user.company.entity.Company;
import br.com.mcampos.ejb.cloudsystem.user.login.Login;
import br.com.mcampos.ejb.cloudsystem.user.person.PersonUtil;
import br.com.mcampos.ejb.entity.system.SystemParameters;
import java.io.Serializable;

public final class DTOFactory implements Serializable {

    public DTOFactory() {
        super();
    }

    public static Login copy(LoginDTO dto) {
        Login login = new Login();
        login.setPassword(dto.getPassword());
        login.setPasswordExpirationDate(dto.getPasswordExpirationDate());
        login.setUserStatus(UserStatusUtil.createEntity(dto.getUserStatus()));
        login.setPerson(PersonUtil.createEntity(dto.getPerson()));
        return login;
    }

    public static LoginDTO copy(Login entity, Boolean mustCopyPerson) {
        if (entity == null) return null;
        LoginDTO login = new LoginDTO();
        login.setUserStatus(UserStatusUtil.copy(entity.getUserStatus()));
        login.setUserId(entity.getUserId());
        if (mustCopyPerson) login.setPerson(PersonUtil.copy(entity.getPerson()));
        return login;
    }

    public static SystemParameters copy(SystemParametersDTO dto) {
        return new SystemParameters(dto.getId(), dto.getDescription(), dto.getValue());
    }

    public static SystemParametersDTO copy(SystemParameters entity) {
        return new SystemParametersDTO(entity.getId(), entity.getDescription(), entity.getValue());
    }

    public static ListLoginDTO copy(Login entity) {
        ListLoginDTO dto;
        dto = new ListLoginDTO();
        dto.setId(entity.getPerson().getId());
        dto.setName(entity.getPerson().getName());
        dto.setUserStatus(UserStatusUtil.copy(entity.getUserStatus()));
        return dto;
    }

    public static AccessLogType copy(AccessLogTypeDTO dto) {
        return new AccessLogType(dto.getId(), dto.getDescription());
    }

    public static AccessLogTypeDTO copy(AccessLogType entity) {
        return new AccessLogTypeDTO(entity.getId(), entity.getDescription());
    }

    public static CompanyTypeDTO copy(CompanyType entity) {
        return new CompanyTypeDTO(entity.getId(), entity.getDescription());
    }

    public static CompanyType copy(CompanyTypeDTO dto) {
        return new CompanyType(dto.getId(), dto.getDescription());
    }

    public static CompanyPositionDTO copy(CompanyPosition entity) {
        return new CompanyPositionDTO(entity.getId(), entity.getDescription());
    }

    public static CompanyPosition copy(CompanyPositionDTO dto) {
        return new CompanyPosition(dto.getId(), dto.getDescription());
    }

    public static Company copy(CompanyDTO dto) {
        Company company = new Company();
        company.setId(dto.getId());
        company.setCompanyType(copy(dto.getCompanyType()));
        return company;
    }

    public static CompanyDTO copy(Company entity) {
        CompanyDTO company = new CompanyDTO();
        company.setCompanyType(copy(entity.getCompanyType()));
        return company;
    }

    public static FormDTO copy(AnotoForm source) {
        if (source == null) return null;
        FormDTO target = new FormDTO(source.getId(), source.getDescription());
        target.setApplication(source.getApplication());
        target.setIcrImage(source.getIcrImage());
        target.setImagePath(source.getImagePath());
        target.setConcatenatePgc(source.getConcatenatePgc());
        return target;
    }

    public static PgcPageAttachment copy(PgcAttachmentDTO dto) {
        PgcPageAttachment entity = new PgcPageAttachment();
        entity.setPgcPage(PgcPageUtil.createEntity(dto.getPgcPage()));
        entity.setSequence(dto.getSequence());
        entity.setType(dto.getType());
        entity.setValue(dto.getValue());
        entity.setBarcodeType(dto.getBarcodeType());
        return entity;
    }

    public static AnotoPageField copy(AnotoPageFieldDTO dto) {
        AnotoPageField target = new AnotoPageField();
        if (dto.getPage() != null) target.setAnotoPage(AnotoPageUtil.createEntity(dto.getPage()));
        target.setHeight(dto.getHeight());
        target.setName(dto.getName());
        target.setIcr(dto.getIcr());
        target.setLeft(dto.getLeft());
        target.setTop(dto.getTop());
        if (dto.getType() != null) target.setType(FieldTypeUtil.createEntity(dto.getType()));
        target.setWidth(dto.getWidth());
        target.setExport(dto.getExport());
        target.setSearchable(dto.getSearchable());
        target.setSequence(dto.getSequence());
        target.setPk(dto.getPk());
        return target;
    }
}
