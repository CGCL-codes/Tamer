package uit.comm.util;

public class BoardUtil {

    /**
    * �����ε����� ���Ѵ�.
    * @param pageIndex �������ε���
    * @return startIndex �����ε���
    */
    public static int getStartIndex(int pageIndex) {
        return getStartIndex(pageIndex, 10);
    }

    /**
    * �����ε����� ���Ѵ�.
    * @param pageIndex �������ε���
    * @param size �Խù�������
    * @return startIndex �����ε���
    */
    public static int getStartIndex(int pageIndex, int size) {
        if (pageIndex == 0) pageIndex = 1;
        int startIndex = (pageIndex - 1) * size + 1;
        return startIndex;
    }

    /**
    * �����ε����� ���Ѵ�.
    * @param pageIndex �������ε���
    * @return startIndex �����ε���
    */
    public static int getStartIndex(String pageIndex) {
        if (pageIndex == null || pageIndex.equals("")) pageIndex = "1";
        return getStartIndex(Integer.parseInt(pageIndex), 10);
    }

    /**
    * �����ε����� ���Ѵ�.
    * @param pageIndex �������ε���
    * @param size �Խù�������
    * @return startIndex �����ε���
    */
    public static int getStartIndex(String pageIndex, int size) {
        if (pageIndex == null || pageIndex.equals("")) pageIndex = "1";
        return getStartIndex(Integer.parseInt(pageIndex), size);
    }

    /**
    * �����ε����� ���Ѵ�.
    * @param pageIndex �������ε���
    * @return startIndex �����ε���
    */
    public static int getEndIndex(int pageIndex) {
        return getEndIndex(pageIndex, 10);
    }

    /**
    * �����ε����� ���Ѵ�.
    * @param pageIndex �������ε���
    * @param size �Խù�������
    * @return endIndex �����ε���
    */
    public static int getEndIndex(int pageIndex, int size) {
        if (pageIndex == 0) pageIndex = 1;
        int endIndex = pageIndex * size;
        return endIndex;
    }

    /**
    * �����ε����� ���Ѵ�.
    * @param pageIndex �������ε���
    * @return endIndex �����ε���
    */
    public static int getEndIndex(String pageIndex) {
        if (pageIndex == null || pageIndex.equals("")) pageIndex = "1";
        return getEndIndex(Integer.parseInt(pageIndex), 10);
    }

    /**
    * �����ε����� ���Ѵ�.
    * @param pageIndex �������ε���
    * @param size �Խù�������
    * @return endIndex �����ε���
    */
    public static int getEndIndex(String pageIndex, int size) {
        if (pageIndex == null || pageIndex.equals("")) pageIndex = "1";
        return getEndIndex(Integer.parseInt(pageIndex), size);
    }

    /**
     * ���ι����� ����¡   [1][2][3][4][5] .. 
     * @param  targetPage       �Խ���������
     * @param  totalListSize    �ѰԽù� ��
     * @param  pageIndex        ������ ��ȣ
     * @return  String
     */
    public static String getPaging(String targetPage, int totalListSize, String pageIndex) {
        if (pageIndex == null || pageIndex.equals("")) pageIndex = "1";
        return getPaging(targetPage, totalListSize, Integer.parseInt(pageIndex));
    }

    /**
     * ���ι����� ����¡   [1][2][3][4][5] .. 
     * @param  targetPage       �Խ���������
     * @param  totalListSize    �ѰԽù� ��
     * @param  pageIndex        ������ ��ȣ
     * @return  String
     */
    public static String getPaging(String targetPage, int totalListSize, int pageIndex) {
        return getPaging(targetPage, totalListSize, pageIndex, 10, 10);
    }

    /**
      * ���ι����� ����¡   [1][2][3][4][5] ..
      * @param targetPage		�Խ���������
      * @param totalListSize	�ѰԽù� ��
      * @param pageIndex		������ ��ȣ
      * @param size				���������� ǥ�õ� �Խù����� (�⺻ 10��)
      * @param pageView         �ִ�� ǥ�õǴ� ������ ũ��
      * @return String
      */
    public static String getPaging(String targetPage, int totalListSize, String pageIndex, int size, int pageView) {
        if (pageIndex == null || pageIndex.equals("")) pageIndex = "1";
        return getPaging(targetPage, totalListSize, Integer.parseInt(pageIndex), size, pageView);
    }

    public static String getPaging(String targetPage, int totalListSize, int pageIndex, int size, int pageView) {
        if (pageIndex == 0) pageIndex = 1;
        int totalPageSize = 0;
        int maxListSize = size;
        int endPage = 0;
        int startPage = 0;
        switch(targetPage.charAt(targetPage.length() - 1)) {
            case '&':
                targetPage += "pageIndex";
                break;
            case '?':
                targetPage += "pageIndex";
                break;
            default:
                if (targetPage.indexOf("?") < 0) targetPage += "?pageIndex"; else targetPage += "&pageIndex";
        }
        totalPageSize = (totalListSize - 1) / maxListSize + 1;
        if (totalListSize == 0) totalPageSize = 0;
        if (totalListSize > 0) {
            startPage = ((pageIndex - 1) / pageView) * pageView + 1;
            endPage = ((pageIndex + pageView - 1) / pageView) * pageView;
            if (endPage > totalPageSize) endPage = totalPageSize;
        }
        String preImage = "./images/main/btn_blitPrev_over.gif";
        String nextImage = "./images/main/btn_blitNext_over.gif";
        String minPage = "<img src='" + preImage + "' border='0' align='absMiddle'>";
        String maxPage = "<img src='" + nextImage + "' border='0' align='absMiddle'>";
        if (startPage > pageView) minPage = "<a href='javascript:goNextPage(" + (startPage - 1) + ")'><img src='" + preImage + "' border='0' align='absMiddle'></a>";
        if (totalPageSize > endPage) maxPage = "<a href='javascript:goNextPage(" + (endPage + 1) + ")'><img src='" + nextImage + "' border='0' align='absMiddle'></a>";
        StringBuffer sb = new StringBuffer();
        sb.append("<table width='100%'><tr align='center'><td>");
        sb.append(minPage + "&nbsp;&nbsp;");
        for (int i = startPage; i <= endPage; i++) {
            String nowPage = "<a href='javascript:goNextPage(" + i + ")'>[" + i + "]</a>";
            if (i == 0) nowPage = "[1]";
            if (i == pageIndex) nowPage = "&nbsp;<b>" + i + "</b>&nbsp;";
            sb.append(nowPage);
        }
        sb.append("&nbsp;&nbsp;" + maxPage);
        sb.append("</td></tr></table>");
        return sb.toString();
    }
}
