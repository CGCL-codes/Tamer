package org.tolven.app;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import org.tolven.core.entity.AccountUser;

public interface AppResolverLocal extends URIResolver {

    public Source resolve(String href, String base) throws TransformerException;

    public void setAccountUser(AccountUser accountUser);

    public AccountUser getAccountUser();
}
