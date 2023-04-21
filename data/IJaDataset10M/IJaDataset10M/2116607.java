package com.kg.emailalbum.mobile.tags;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.kg.emailalbum.mobile.R;
import com.kg.emailalbum.mobile.util.BitmapUtil;

public class TagFilter implements Comparable<TagFilter> {

    public Tag tag = null;

    public TagFilterType type = null;

    public TagFilter(Tag tag, TagFilterType type) {
        this.tag = tag;
        this.type = type;
    }

    public enum TagFilterType {

        MANDATORY {

            @Override
            public Drawable getDrawable(Context ctx) {
                return initDrawable(ctx, this, R.drawable.ic_add);
            }

            @Override
            public Integer getSortOrder() {
                return 0;
            }
        }
        , OPTIONAL {

            @Override
            public Drawable getDrawable(Context ctx) {
                return initDrawable(ctx, this, R.drawable.ic_optional);
            }

            @Override
            public Integer getSortOrder() {
                return 1;
            }
        }
        , HIDE {

            @Override
            public Drawable getDrawable(Context ctx) {
                return initDrawable(ctx, this, R.drawable.delete);
            }

            @Override
            public Integer getSortOrder() {
                return 2;
            }
        }
        ;

        private static final int TAG_FILTER_TYPE_ICON_SIZE = 16;

        private static final Map<TagFilterType, Drawable> mDrawables = new HashMap<TagFilterType, Drawable>();

        public abstract Drawable getDrawable(Context ctx);

        public abstract Integer getSortOrder();

        /**
         * @param ctx
         * @return
         */
        private static Drawable initDrawable(Context ctx, TagFilterType tagType, int drawableId) {
            Drawable drawable = mDrawables.get(tagType);
            if (drawable == null) {
                drawable = ctx.getResources().getDrawable(drawableId);
                drawable.setBounds(0, 0, (int) (TAG_FILTER_TYPE_ICON_SIZE * BitmapUtil.getDensity(ctx)), (int) (TAG_FILTER_TYPE_ICON_SIZE * BitmapUtil.getDensity(ctx)));
                mDrawables.put(tagType, drawable);
            }
            return drawable;
        }
    }

    @Override
    public int compareTo(TagFilter anotherTagFilter) {
        if (this.type != anotherTagFilter.type) {
            return this.type.getSortOrder().compareTo(anotherTagFilter.type.getSortOrder());
        }
        return this.tag.compareTo(anotherTagFilter.tag);
    }
}
