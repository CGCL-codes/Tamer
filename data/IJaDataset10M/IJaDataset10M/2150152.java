package org.esprit.ocm.server.dao;

import org.esprit.ocm.dto.impl.AwsCredentialsDto;
import org.esprit.ocm.dto.impl.ServerDto;
import org.esprit.ocm.server.dao.common.IDao;

public interface AwsCredentialsDao extends IDao<AwsCredentialsDto> {

    public AwsCredentialsDto findCredentialByServer(ServerDto _o);
}
