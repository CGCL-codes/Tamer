package ru.xeden.web.html;

import ru.xeden.web.core.services.IRequest;

public interface IPageProvider {

    IPage getPage(IRequest request);
}
