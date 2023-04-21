package net.sf.josceleton.core.impl.entity;

import net.sf.josceleton.core.api.entity.User;
import com.google.inject.assistedinject.Assisted;

public interface UserFactory {

    User create(@Assisted("id") int id, @Assisted("osceletonId") int osceletonId);
}
