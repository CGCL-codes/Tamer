package com.cms.bi.bistat.service;

import com.d3.bi.basecommon.vo.D3biValueObject;

public interface MailManager {

    public D3biValueObject viewInfo(D3biValueObject v);

    public D3biValueObject listInfo(D3biValueObject v);

    public D3biValueObject saveInfo(D3biValueObject v);

    public D3biValueObject removeInfo(D3biValueObject v);

    public D3biValueObject login(D3biValueObject v);

    public D3biValueObject register(D3biValueObject v);
}
