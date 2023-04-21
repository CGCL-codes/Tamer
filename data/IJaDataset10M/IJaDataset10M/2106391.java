package org.xblackcat.rojac.gui.view.message;

import org.apache.commons.lang3.ArrayUtils;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.service.options.Property;
import javax.swing.*;

/**
 * @author xBlackCat
 */
final class MarkItem {

    private final Integer id;

    private Mark[] marks = new Mark[0];

    private final User user;

    private final int userId;

    MarkItem(NewRating r) {
        this(r.getId(), Property.RSDN_USER_ID.get(), null);
    }

    MarkItem(int userId, User user) {
        this(null, userId, user);
    }

    private MarkItem(Integer id, int userId, User user) {
        this.id = id;
        this.userId = userId;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public Icon getMarkIcons() {
        return new MarksIcon(marks);
    }

    public void addMark(Mark m) {
        marks = ArrayUtils.add(marks, m);
    }

    public Integer getUserId() {
        return userId;
    }

    public boolean isNewRate() {
        return id != null;
    }

    public User getUser() {
        return user;
    }
}
