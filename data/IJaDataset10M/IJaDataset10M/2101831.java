package com.abs.orm.convert;

import java.io.Serializable;
import com.abs.orm.model.file.ActionApplicationContextFileModel;
import com.abs.orm.model.file.ActionFileModel;
import com.abs.orm.model.file.DaoFileModel;
import com.abs.orm.model.file.DaoImplFileModel;
import com.abs.orm.model.file.HbmFileModel;
import com.abs.orm.model.file.ModelFileModel;
import com.abs.orm.model.file.ServiceFileModel;
import com.abs.orm.model.file.ServiceImplFileModel;
import com.abs.orm.model.file.SpringApplicationContextFileModel;
import com.abs.orm.model.file.config.ActionConfigModel;
import com.abs.orm.model.file.config.DaoConfigModel;
import com.abs.orm.model.file.config.DaoImplConfigModel;
import com.abs.orm.model.file.config.ModelConfigModel;
import com.abs.orm.model.file.config.ServiceConfigModel;
import com.abs.orm.model.file.config.ServiceImplConfigModel;
import com.ads.orm.db.model.Table;

public interface Convert extends Serializable {

    public abstract ModelFileModel convert(Table table, ModelConfigModel config);

    public abstract DaoFileModel convert(Table table, ModelFileModel model, DaoConfigModel config);

    public abstract DaoImplFileModel convert(Table table, ModelFileModel model, DaoFileModel dao, DaoImplConfigModel config);

    public abstract ServiceFileModel convert(ModelFileModel model, DaoFileModel dao, ServiceConfigModel config);

    public abstract ServiceImplFileModel convert(ModelFileModel model, DaoFileModel dao, ServiceFileModel service, ServiceImplConfigModel config);

    public abstract ActionFileModel convert(ModelFileModel model, DaoFileModel dao, ServiceFileModel service, ActionConfigModel config);

    public abstract HbmFileModel convert(Table table, ModelFileModel model);

    public abstract SpringApplicationContextFileModel convert(DaoFileModel dao, DaoImplFileModel daoImpl);

    public abstract SpringApplicationContextFileModel convert(ServiceFileModel service, ServiceImplFileModel serviceImpl);

    public abstract ActionApplicationContextFileModel convert(DaoFileModel dao, DaoImplFileModel daoImpl, ServiceFileModel service, ServiceImplFileModel serviceImpl);
}
