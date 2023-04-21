package chapterRape;

/**
 * ChapterRapper - < . >
 * 
 * @author J. Villalta (C)
 * @version 0509.libs
 * 
 * Copyright (C) 2009  J. Villalta.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
public class ChapterRapper {

    protected String htmlBody, keyWord;

    protected String splitedBody[];

    protected ChapterRapperCore rCore;

    /**
     * Constructor for objects of class ChapterRapper
     */
    public ChapterRapper(String htmlBody, String keyWord) {
        this.htmlBody = htmlBody;
        this.keyWord = keyWord;
        rCore = new ChapterRapperCoreModels();
        rCore.selectConfiguration(1);
    }

    protected ChapterRappedContainer createChapterRContainer() {
        return new ChapterRappedContainer();
    }

    public void selectNextConfiguration() {
        rCore.selectNextConfiguration();
    }

    public void selectConfiguration(int i) {
        rCore.selectConfiguration(i);
    }

    public int getCurrentConfiguration() {
        return rCore.getCurrentConfiguration();
    }

    public ChapterRappedContainer getChapterRappedCont(String LINKS_FILTER[]) {
        ChapterRappedContainer container = createChapterRContainer();
        rCore.fillChapterRappedCont(container, htmlBody, LINKS_FILTER);
        return container;
    }
}
