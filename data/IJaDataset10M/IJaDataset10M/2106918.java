package com.sitechasia.webx.core.utils.image;

/**
 * 图形相关常量
 *
 * @author zane
 * @author todd
 * @version 1.2 , 2008/5/7
 * @since JDK1.5
 */
public interface ImageConstants {

    /**
	 * 图形格式
	 *
	 * @author zane
	 * @since 1.0
	 */
    public interface FormatName {

        /**
		 * BMP格式
		 */
        public String BMP = "BMP";

        /**
		 * WBMP格式
		 */
        public String WBMP = "WBMP";

        /**
		 * JPEG格式
		 */
        public String JPEG = "JPEG";

        /**
		 * PNG格式
		 */
        public String PNG = "PNG";
    }

    /**
	 * 水印布局常量
	 *
	 * @author zane
	 * @since 1.0
	 *
	 */
    public interface WatermarkLayout {

        /**
		 * 底部居中
		 */
        public String BOTTOM_CENTER = "BOTTOM_CENTER";

        /**
		 * 底部左对齐
		 */
        public String BOTTOM_LEFT = "BOTTOM_LEFT";

        /**
		 * 底部右对齐
		 */
        public String BOTTOM_RIGHT = "BOTTOM_RIGHT";

        /**
		 * 中部居中
		 */
        public String MIDDLE_CENTER = "MIDDLE_CENTER";

        /**
		 * 中部左对齐
		 */
        public String MIDDLE_LEFT = "MIDDLE_LEFT";

        /**
		 * 中部右对齐
		 */
        public String MIDDLE_RIGHT = "MIDDLE_RIGHT";

        /**
		 * 顶部居中
		 */
        public String TOP_CENTER = "TOP_CENTER";

        /**
		 * 顶部左对齐
		 */
        public String TOP_LEFT = "TOP_LEFT";

        /**
		 * 顶部右对齐
		 */
        public String TOP_RIGHT = "TOP_RIGHT";
    }
}
