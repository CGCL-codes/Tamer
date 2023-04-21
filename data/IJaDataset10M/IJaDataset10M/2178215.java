package org.jgentleframework.utils;

/**
 * The Class ConverUtils.
 * 
 * @author Quoc Chung - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Jun 8, 2009
 */
public abstract class ConvertUtils {

    /**
	 * To text not marked.
	 * 
	 * @param text
	 *            the text
	 * @return the string
	 */
    public static String toTextNotMarked(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch(c) {
                case 'À':
                case 'Á':
                case 'Ả':
                case 'Ã':
                case 'Ạ':
                case 'Ă':
                case 'Ằ':
                case 'Ắ':
                case 'Ẳ':
                case 'Ẵ':
                case 'Ặ':
                case 'Â':
                case 'Ầ':
                case 'Ấ':
                case 'Ẩ':
                case 'Ẫ':
                case 'Ậ':
                    builder.append('A');
                    break;
                case 'à':
                case 'á':
                case 'ả':
                case 'ã':
                case 'ạ':
                case 'ă':
                case 'ằ':
                case 'ắ':
                case 'ẳ':
                case 'ẵ':
                case 'ặ':
                case 'â':
                case 'ầ':
                case 'ấ':
                case 'ẩ':
                case 'ẫ':
                case 'ậ':
                    builder.append('a');
                    break;
                case 'Đ':
                    builder.append('D');
                    break;
                case 'đ':
                    builder.append('d');
                    break;
                case 'È':
                case 'É':
                case 'Ẻ':
                case 'Ẽ':
                case 'Ẹ':
                case 'Ê':
                case 'Ề':
                case 'Ế':
                case 'Ể':
                case 'Ễ':
                case 'Ệ':
                    builder.append('E');
                    break;
                case 'è':
                case 'é':
                case 'ẻ':
                case 'ẽ':
                case 'ẹ':
                case 'ê':
                case 'ề':
                case 'ế':
                case 'ể':
                case 'ễ':
                case 'ệ':
                    builder.append('e');
                    break;
                case 'Ì':
                case 'Í':
                case 'Ỉ':
                case 'Ĩ':
                case 'Ị':
                    builder.append('I');
                    break;
                case 'ì':
                case 'í':
                case 'ỉ':
                case 'ĩ':
                case 'ị':
                    builder.append('i');
                    break;
                case 'Ò':
                case 'Ó':
                case 'Ỏ':
                case 'Õ':
                case 'Ọ':
                case 'Ô':
                case 'Ồ':
                case 'Ố':
                case 'Ổ':
                case 'Ỗ':
                case 'Ộ':
                case 'Ơ':
                case 'Ờ':
                case 'Ớ':
                case 'Ở':
                case 'Ỡ':
                case 'Ợ':
                    builder.append('O');
                    break;
                case 'ò':
                case 'ó':
                case 'ỏ':
                case 'õ':
                case 'ọ':
                case 'ô':
                case 'ồ':
                case 'ố':
                case 'ổ':
                case 'ỗ':
                case 'ộ':
                case 'ơ':
                case 'ờ':
                case 'ớ':
                case 'ở':
                case 'ỡ':
                case 'ợ':
                    builder.append('o');
                    break;
                case 'Ù':
                case 'Ú':
                case 'Ủ':
                case 'Ũ':
                case 'Ụ':
                case 'Ư':
                case 'Ừ':
                case 'Ứ':
                case 'Ử':
                case 'Ữ':
                case 'Ự':
                    builder.append('U');
                    break;
                case 'ù':
                case 'ú':
                case 'ủ':
                case 'ũ':
                case 'ụ':
                case 'ư':
                case 'ừ':
                case 'ứ':
                case 'ử':
                case 'ữ':
                case 'ự':
                    builder.append('u');
                    break;
                case 'Ỳ':
                case 'Ý':
                case 'Ỷ':
                case 'Ỹ':
                case 'Ỵ':
                    builder.append('Y');
                    break;
                case 'ỳ':
                case 'ý':
                case 'ỷ':
                case 'ỹ':
                case 'ỵ':
                    builder.append('y');
                    break;
                default:
                    builder.append(c);
                    break;
            }
        }
        return builder.toString();
    }
}
