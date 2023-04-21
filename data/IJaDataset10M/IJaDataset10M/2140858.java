package com.apusic.ebiz.framework.core.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component(value = "ajaxRestService")
public class AjaxRestServiceImpl<T> extends DefaultAjaxRestServiceImpl<T> implements AjaxRestService<T> {
}
