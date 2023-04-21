package org.nomadpim.core.ui.service;

import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.entity.IType;
import org.nomadpim.core.util.text.IFormatter;

public interface IFormatterService {

    IFormatter<IEntity> getFormatter(IType type);
}
