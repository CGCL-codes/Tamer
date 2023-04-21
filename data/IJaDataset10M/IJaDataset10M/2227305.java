package ipman.app.base.serviceimpl;

import java.util.List;
import java.util.ArrayList;
import org.springframework.validation.Errors;
import ipman.app.base.dao.*;
import ipman.app.base.dto.*;
import ipman.app.base.assembler.*;
import ipman.app.base.service.*;
import ipman.app.base.domain.*;
import ipman.app.base.validator.*;

public class UserServiceImpl implements UserService {

    private UserDao dao;

    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    /**
    *
    */
    public UserDto loadUserById(long id) {
        UserAssembler assembler = new UserAssembler();
        User resultDao = dao.loadUserById(id);
        UserDto resultDto = assembler.convertToDto(resultDao);
        return resultDto;
    }

    /**
    *
    */
    public List<UserDto> loadAllUsers() {
        UserAssembler assembler = new UserAssembler();
        List<UserDto> resultDto = new ArrayList<UserDto>();
        List<User> results = dao.loadAllUsers();
        for (User domain : results) {
            resultDto.add(assembler.convertToDto(domain));
        }
        return resultDto;
    }

    /**
    *
    */
    public UserDto createUser(UserDto dto) {
        UserAssembler assembler = new UserAssembler();
        User domain = assembler.convertToDomain(dto);
        UserValidator validator = new UserValidator();
        long id = dao.createUser(domain);
        dto.setId(id);
        return dto;
    }

    /**
    *
    */
    public UserDto updateUser(UserDto dto) {
        UserAssembler assembler = new UserAssembler();
        User domain = assembler.convertToDomain(dto);
        boolean res = dao.updateUser(domain);
        if (res) {
            return dto;
        }
        return null;
    }

    /**
    *
    */
    public boolean deleteUser(UserDto dto) {
        UserAssembler assembler = new UserAssembler();
        User domain = assembler.convertToDomain(dto);
        boolean res = dao.deleteUser(domain);
        return res;
    }

    /**
    *
    */
    public boolean deleteUserById(long id) {
        return true;
    }
}
