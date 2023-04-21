package com.khotyn.heresy.validator;

import java.util.regex.Pattern;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.khotyn.heresy.bean.Album;

/**
 * 相册属性的验证器
 * 
 * @author 王长乐
 * 
 */
public class AlbumValidator implements Validator {

    public boolean supports(Class arg0) {
        return Album.class.equals(arg0);
    }

    public void validate(Object command, Errors errors) {
        Album album = (Album) command;
        ValidationUtils.rejectIfEmpty(errors, "albumName", "albumName is required.", "相册名字不能为空");
        if (album.getAlbumName().length() > 100) {
            errors.rejectValue("albumName", "album_name_length_overflow", "请不要输入SQL关键字");
        }
        if (Pattern.matches("select|update|delete|exec|count|'|\"|=|;|>|<|%", album.getAlbumName())) {
            errors.rejectValue("albumName", "albumname_name_SQLInjection", "请不要输入SQL关键字");
        }
        if (album.getAlbumDescription().length() > 1000) {
            errors.rejectValue("albumDescription", "pictureDetail_description_length_overflow", "您输入的描述长度过长");
        }
        if (Pattern.matches("select|update|delete|exec|count|'|\"|=|;|>|<|%", album.getAlbumDescription())) {
            errors.rejectValue("albumDescription", "album_description_SQLInjection", "请不要输入SQL关键字");
        }
    }
}
