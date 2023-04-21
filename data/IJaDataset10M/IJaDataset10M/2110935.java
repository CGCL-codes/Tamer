package com.ibatis.sqlmap.engine.mapping.sql.dynamic.elements;

import org.apache.ibatis.reflection.MetaObject;

public class IterateTagHandler extends BaseTagHandler {

    public int doStartFragment(SqlTagContext ctx, SqlTag tag, Object parameterObject) {
        IterateContext iterate = (IterateContext) ctx.getAttribute(tag);
        if (iterate == null) {
            IterateContext parentIterate = ctx.peekIterateContext();
            ctx.pushRemoveFirstPrependMarker(tag);
            Object collection;
            String prop = tag.getPropertyAttr();
            if (prop != null && !prop.equals("")) {
                if (null != parentIterate && parentIterate.isAllowNext()) {
                    parentIterate.next();
                    parentIterate.setAllowNext(false);
                    if (!parentIterate.hasNext()) {
                        parentIterate.setFinal(true);
                    }
                }
                if (parentIterate != null) {
                    prop = parentIterate.addIndexToTagProperty(prop);
                }
                collection = MetaObject.forObject(parameterObject).getValue(prop);
            } else {
                collection = parameterObject;
            }
            iterate = new IterateContext(collection, tag, parentIterate);
            iterate.setProperty(null == prop ? "" : prop);
            ctx.setAttribute(tag, iterate);
            ctx.pushIterateContext(iterate);
        } else if ("iterate".equals(tag.getRemoveFirstPrepend())) {
            ctx.reEnableRemoveFirstPrependMarker();
        }
        if (iterate.hasNext()) {
            return INCLUDE_BODY;
        } else {
            return SKIP_BODY;
        }
    }

    public int doEndFragment(SqlTagContext ctx, SqlTag tag, Object parameterObject, StringBuffer bodyContent) {
        IterateContext iterate = (IterateContext) ctx.getAttribute(tag);
        if (iterate.hasNext() || iterate.isFinal()) {
            if (iterate.isAllowNext()) {
                iterate.next();
            }
            if (bodyContent.toString().trim().length() > 0) {
                if (iterate.someSubElementsHaveContent()) {
                    if (tag.isConjunctionAvailable()) {
                        bodyContent.insert(0, tag.getConjunctionAttr());
                    }
                } else {
                    iterate.setPrependEnabled(true);
                    if (tag.isOpenAvailable()) {
                        bodyContent.insert(0, tag.getOpenAttr());
                    }
                }
                iterate.setSomeSubElementsHaveContent(true);
            }
            if (iterate.isLast() && iterate.someSubElementsHaveContent()) {
                if (tag.isCloseAvailable()) {
                    bodyContent.append(tag.getCloseAttr());
                }
            }
            iterate.setAllowNext(true);
            if (iterate.isFinal()) {
                return super.doEndFragment(ctx, tag, parameterObject, bodyContent);
            } else {
                return REPEAT_BODY;
            }
        } else {
            return super.doEndFragment(ctx, tag, parameterObject, bodyContent);
        }
    }

    public void doPrepend(SqlTagContext ctx, SqlTag tag, Object parameterObject, StringBuffer bodyContent) {
        IterateContext iterate = (IterateContext) ctx.getAttribute(tag);
        if (iterate.isPrependEnabled()) {
            super.doPrepend(ctx, tag, parameterObject, bodyContent);
            iterate.setPrependEnabled(false);
        }
    }

    public boolean isPostParseRequired() {
        return true;
    }
}
